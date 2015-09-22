package uk.ac.ebi.jmzidml.xml.xxindex;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import psidev.psi.tools.xxindex.index.IndexElement;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Rui Wang
 * @version $Id$
 */
public class MemoryMappedXmlElementExtractor {

    private static Logger log = LoggerFactory.getLogger(MemoryMappedXmlElementExtractor.class);
    // XML 1.1 specs
    // [3]  S            ::=  (#x20 | #x9 | #xD | #xA)+             /* spaces, carriage returns, line feeds, or tabs */
    // [23] XMLDecl      ::=  '<?xml' VersionInfo EncodingDecl? SDDecl? S? '?>'
    // [24] VersionInfo  ::=  S 'version' Eq ("'" VersionNum "'" | '"' VersionNum '"')
    // [25] Eq           ::=  S? '=' S?
    // [80] EncodingDecl ::=  S 'encoding' Eq ('"' EncName '"' | "'" EncName "'" )
    // [81] EncName      ::=  [A-Za-z] ([A-Za-z0-9._] | '-')*        /* Encoding name contains only Latin characters */
    protected static final Pattern xmlHeader = Pattern.compile(".*<\\?xml.+\\?>.*", Pattern.DOTALL);
    protected static final Pattern xmlEnc = Pattern.compile(".*encoding\\s*=\\s*[\"']([A-Za-z]([A-Za-z0-9._]|[-])*)[\"'](.*)", Pattern.DOTALL);

    private boolean useSystemDefaultEncoding;
    private Charset encoding;


    ////////////////////
    // Constructor

    /**
     * Default constructor setting the default character encoding to 'ASCII'.
     */
    public MemoryMappedXmlElementExtractor() {
        setUseSystemDefaultEncoding(false);
        setEncoding(Charset.forName("ASCII"));
    }

    /**
     * Constructor overwriting the default character encoding with the specified one.
     *
     * @param encoding The Charset to use to translate the read bytes.
     */
    @SuppressWarnings(value = "unused")
    public MemoryMappedXmlElementExtractor(Charset encoding) {
        this();
        setEncoding(encoding);
    }

    ////////////////////
    // Getter & Setter

    /**
     * @return The currently set encoding.
     */
    public Charset getEncoding() {
        return encoding;
    }

    /**
     * @param encoding The encoding to use when converting the read byte array into a String.
     */
    public void setEncoding(Charset encoding) {
        this.encoding = encoding;
    }

    /**
     * This method will try to find and set a Charset for the given String.
     *
     * @param encoding The encoding to use when converting the read byte array into a String.
     * @return 0 is returned on success, -1 if the specified encoding is not valid and -2 if
     * the specified encoding is not supported by this virtual machine.
     */
    public int setEncoding(String encoding) {
        int result;
        try {
            this.encoding = Charset.forName(encoding);
            result = 0;
        } catch (IllegalCharsetNameException icne) {
            log.error("Illegal encoding: " + encoding, icne);
            result = -1;
        } catch (UnsupportedCharsetException ucne) {
            log.error("Unsupported encoding: " + encoding, ucne);
            result = -2;
        }
        return result;
    }

    /**
     * @return Flag whether the system default encoding is used for decoding.
     */
    public boolean isUseSystemDefaultEncoding() {
        return useSystemDefaultEncoding;
    }

    /**
     * A boolean flag which defines if the system default character
     * encoding is to be used for decoding the read bytes.
     * Note: If the flag is set any other specified encoding will be ignored!
     *
     * @param useSystemDefaultEncoding A flag whether to use the system default
     *                                 character encoding.
     */
    public void setUseSystemDefaultEncoding(boolean useSystemDefaultEncoding) {
        this.useSystemDefaultEncoding = useSystemDefaultEncoding;
    }

    ////////////////////
    // Methods

    /**
     * Same as readString(long from, long to, File file), but start and stop wrapped in a ByteRange object.
     *
     * @param br          the ByteRange to read.
     * @param inputStream the file to read from.
     * @return the XML element including start and stop tag in a String.
     * @throws java.io.IOException if an I/O error occurs while reading.
     */
    public String readString(IndexElement br, InputStream inputStream) throws IOException {
        return readString(br.getStart(), br.getStop(), inputStream);
    }

    /**
     * Retrieves bytes from the specified file from position 'from' for a
     * length of 'to - from' bytes.
     *
     * @param from The position from where to start reading.
     * @param to   The position to which to read.
     * @param inputStream The file input stream to read from.
     * @return The read byte array.
     * @throws IOException              If a I/O Exception during the reading process occurred.
     * @throws IllegalArgumentException If the range specified to read (to - from)
     *                                  is to big (&gt; Integer.MAX_VALUE characters).
     */
    public byte[] readBytes(long from, long to, InputStream inputStream) throws IOException {
        byte[] bytes;

        Long length = to - from;
        if (length > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Can not read more than " + Integer.MAX_VALUE + " bytes!");
        }
        bytes = new byte[length.intValue()];

        // read into buffer
        inputStream.skip(from);
        inputStream.read(bytes, 0, length.intValue());

        return bytes;
    }

    /**
     * Convenience method that combines the methods: readBytes(), removeZeroBytes() and bytes2String().
     * <p>
     * Read a String representing a XML element from the specified file (which will be opened read-only).
     * It will read from position 'from' for length 'to - from'.
     *
     * @param from The byte position of the start (incl. beginning of start tag) of the XML element.
     * @param to   The byte position of the end (incl. end of closing tag) of the XML element.
     * @param inputStream The file input stream to read from.
     * @return The XML element including start and stop tag in a String.
     */
    public String readString(long from, long to, InputStream inputStream) throws IOException {
        // retrieve the bytes from the given range in the file
        byte[] bytes = readBytes(from, to, inputStream);

        // remove all zero bytes (Mac filling bytes)
        byte[] newBytes = removeZeroBytes(bytes);

        // create a String from the rest using the given encoding if specified
        return bytes2String(newBytes);
    }

    /**
     * This method represents the default case of the this#detectFileEncoding(URL, int)
     * method limiting the number of read bytes to 1000.
     *
     * @param inputStream The file input stream to read from.
     * @return A String representing the Charset detected for the provided
     * file or null if no character encoding could be determined.
     * @throws IOException If the specified location could not be opened for reading.
     */
    public String detectFileEncoding(InputStream inputStream) throws IOException {
        return detectFileEncoding(inputStream, 1000);
    }

    /**
     * This method reads up to maxReadLength bytes from the file specified by
     * fileLocation and will try to detect the character encoding.
     * This simple detection is assuming a file according to XML 1.1 specs, where
     * the encoding should be provided in the XML header/prolog. It will only try
     * to parse this information from the read bytes.
     *
     * @param inputStream  The file input stream to read from.
     * @param maxReadLength The maximum number of bytes to read from the file.
     * @return A String representing the Charset detected for the provided
     * file or null if no character encoding could be determined.
     * @throws IOException If the specified location could not be opened for reading.
     */
    public String detectFileEncoding(InputStream inputStream, int maxReadLength) throws IOException {

        // read a bit of the input file and check if it contains a XML header
        int length = inputStream.available();

        // read a maximum of maxReadLength bytes
        byte[] bytes;
        if (length > maxReadLength) {
            bytes = new byte[maxReadLength];
        } else {
            bytes = new byte[length];
        }
        // fill the byte buffer
        inputStream.read(bytes);

        // convert the bytes to String using ASCII
        String fileStart = new String(bytes, "ASCII");

        // first check if there is a XML header
        Matcher mHead = MemoryMappedXmlElementExtractor.xmlHeader.matcher(fileStart);
        if (!mHead.matches()) {
            // no XML header not found
            log.debug("No XML header found for input");
            return null;
        }

        Matcher mEnc = MemoryMappedXmlElementExtractor.xmlEnc.matcher(fileStart);
        if (!mEnc.matches()) {
            return null;
        }
        if (mEnc.groupCount() < 1) {
            return null;
        }

        String charsetName = mEnc.group(1);
        log.debug("Detected charset " + charsetName + " for input");
        return charsetName;
    }

    /**
     * Convenience method to strip the byte array of zero bytes (such as filling
     * bytes used on some Mac OSX versions).
     *
     * @param bytes byte array that may contain zero bytes (\u0000)
     * @return byte array free of zero bytes.
     */
    public byte[] removeZeroBytes(byte[] bytes) {
        // This code is pretty low-level and may seem peculiar.
        // The reason for coding it this way is performance. If a
        // collection is used here rather than a staging array,
        // the performance drops immensely.
        byte[] temp = new byte[bytes.length];
        int count = 0;
        for (byte aByte : bytes) {
            if (aByte != (byte) 0) {
                temp[count] = aByte;
                count++;
            }
        }

        // Now we know how many bytes we retrieved,
        // so create a smaller array for the final result
        // if necessary.
        byte[] result;
        if (count != bytes.length) {
            result = new byte[count];
            System.arraycopy(temp, 0, result, 0, count);
        } else {
            result = temp;
        }

        return result;
    }

    /**
     * Converts the specified byte array into a String, using the encoding
     * defined for this StandardXmlElementExtractor.
     *
     * @param bytes The byte array to convert into a String.
     * @return The String representation of the byte array.
     * @throws IllegalStateException                If no character encoding is available.
     * @throws java.io.UnsupportedEncodingException if the set character encoding is not supported.
     */
    public String bytes2String(byte[] bytes) throws UnsupportedEncodingException {

        // if the user prefers the system default character encoding our life is easy
        if (isUseSystemDefaultEncoding()) {
            if (log.isDebugEnabled()) log.info("Using system default for encoding.");
            return new String(bytes);
        }
        // if not we use the set encoding

        // quick check that there is one
        if (getEncoding() == null) {
            throw new IllegalStateException("No character encoding available to convert the byte array!");
        }

        // use the encoding to translate the byte array into a String
        return new String(bytes, getEncoding().name());
    }
}
