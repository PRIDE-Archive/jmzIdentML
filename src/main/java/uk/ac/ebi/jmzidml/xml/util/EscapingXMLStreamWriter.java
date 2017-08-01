package uk.ac.ebi.jmzidml.xml.util;

import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import uk.ac.ebi.jmzidml.model.utils.MzIdentMLVersion;

/**
 * Delegating {@link javax.xml.stream.XMLStreamWriter} that filters out UTF-8 characters that
 * are illegal in XML.
 * <p>
 * See forum post: http://glassfish.10926.n7.nabble.com/Escaping-illegal-characters-during-marshalling-td59751.html#a20090044
 *
 * @author Erik van Zijst
 */
public class EscapingXMLStreamWriter implements XMLStreamWriter {

    private final XMLStreamWriter writer;
    private String charEncoding;
    private MzIdentMLVersion version;

    public EscapingXMLStreamWriter(XMLStreamWriter writer, MzIdentMLVersion version) {

        if (null == writer) {
            throw new IllegalArgumentException("null");
        } else {
            this.writer = writer;
        }
        this.charEncoding = "UTF-8";
        this.version = version;
    }

    public EscapingXMLStreamWriter(XMLStreamWriter writer, MzIdentMLVersion version, String encoding) {
        this(writer, version);
        if (encoding != null) {
            this.charEncoding = encoding;
        }
    }

    public void writeStartElement(String localName) throws XMLStreamException {
        writer.writeStartElement(localName);
    }

    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        writer.writeStartElement(version.getNameSpace(), localName);
    }

    public void writeStartElement(String prefix, String localName, String namespaceURI)
            throws XMLStreamException {
        writer.writeStartElement("", localName, version.getNameSpace());
    }

    public void writeEmptyElement(String namesapceURI, String localName) throws XMLStreamException {
        writer.writeEmptyElement(version.getNameSpace(), localName);
    }

    public void writeEmptyElement(String prefix, String localName, String namesapceURI)
            throws XMLStreamException {
        writer.writeEmptyElement("", localName, version.getNameSpace());
    }

    public void writeEmptyElement(String localName) throws XMLStreamException {
        writer.writeEmptyElement(localName);
    }

    public void writeEndElement() throws XMLStreamException {
        writer.writeEndElement();
    }

    public void writeEndDocument() throws XMLStreamException {
        writer.writeEndDocument();
    }

    public void close() throws XMLStreamException {
        writer.close();
    }

    public void flush() throws XMLStreamException {
        writer.flush();
    }

    public void writeAttribute(String localName, String value) throws XMLStreamException {
        writer.writeAttribute(localName, EscapingXMLUtilities.escapeCharacters(value));
    }

    public void writeAttribute(String prefix, String namespaceUri, String localName, String value)
            throws XMLStreamException {
        writer.writeAttribute("", version.getNameSpace(), localName, EscapingXMLUtilities.escapeCharacters(value));
    }

    public void writeAttribute(String namespaceUri, String localName, String value)
            throws XMLStreamException {
        writer.writeAttribute(version.getNameSpace(), localName, EscapingXMLUtilities.escapeCharacters(value));
    }

    public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
        writer.writeNamespace("", version.getNameSpace());
    }

    public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
        writer.writeDefaultNamespace(version.getNameSpace());
    }

    public void writeComment(String s) throws XMLStreamException {
        writer.writeComment(s);
    }

    public void writeProcessingInstruction(String s) throws XMLStreamException {
        writer.writeProcessingInstruction(s);
    }

    public void writeProcessingInstruction(String s, String s1)
            throws XMLStreamException {
        writer.writeProcessingInstruction(s, s1);
    }

    public void writeCData(String s) throws XMLStreamException {
        writer.writeCData(EscapingXMLUtilities.escapeCharacters(s));
    }

    public void writeDTD(String s) throws XMLStreamException {
        writer.writeDTD(s);
    }

    public void writeEntityRef(String s) throws XMLStreamException {
        writer.writeEntityRef(s);
    }

    public void writeStartDocument() throws XMLStreamException {
        writer.writeStartDocument(this.charEncoding,"1.0");
    }

    public void writeStartDocument(String s) throws XMLStreamException {
        writer.writeStartDocument(s);
    }

    public void writeStartDocument(String s, String s1)
            throws XMLStreamException {
        writer.writeStartDocument(s, s1);
    }

    public void writeCharacters(String s) throws XMLStreamException {
        writer.writeCharacters(EscapingXMLUtilities.escapeCharacters(s));
    }

    public void writeCharacters(char[] chars, int start, int len)
            throws XMLStreamException {
        writer.writeCharacters(EscapingXMLUtilities.escapeCharacters(new String(chars, start, len)));
    }

    public String getPrefix(String uri) throws XMLStreamException {
        return writer.getPrefix(uri);
    }

    public void setPrefix(String prefix, String uri) throws XMLStreamException {
        writer.setPrefix(prefix, uri);
    }

    public void setDefaultNamespace(String s) throws XMLStreamException {
        writer.setDefaultNamespace(version.getNameSpace());
    }

    public void setNamespaceContext(NamespaceContext namespaceContext)
            throws XMLStreamException {
        writer.setNamespaceContext(namespaceContext);        
    }

    public NamespaceContext getNamespaceContext() {
        return writer.getNamespaceContext();
    }

    public Object getProperty(String s) throws IllegalArgumentException {
        return writer.getProperty(s);
    }
}