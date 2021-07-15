package uk.ac.ebi.jmzidml.xml.util;

/**
 * Created with IntelliJ IDEA.
 * User: rcote
 * Date: 04/04/13
 * Time: 14:39
 * To change this template use File | Settings | File Templates.
 */
public class EscapingXMLUtilities {

    public static final char substitute = '\uFFFD';

    private EscapingXMLUtilities() {
    }

    /**
     // excluded control characters
     \u0000 Null character
     \u0001 Start of header
     \u0002 Start of text
     \u0003 End of text
     \u0004 End of transmission
     \u0005 Enquiry
     \u0006 Positive acknowledge
     \u0007 Alert (bell)
     \u0008 Backspace
     \u000B Vertical tab
     \u000C Form feed
     \u000E Shift out
     \u000F Shift in
     \u0010 Data link escape
     \u0011 Device control 1 (XON)
     \u0012 Device control 2 (tape on)
     \u0013 Device control 3 (XOFF)
     \u0014 Device control 4 (tape off)
     \u0015 Negative acknowledgement
     \u0016 Synchronous idle
     \u0017 End of transmission block
     \u0018 Cancel
     \u0019 End of medium
     \u001A Substitute
     \u001B Escape
     \u001C File separator (Form separator)
     \u001D Group separator
     \u001E Record separator
     \u001F Unit separator

     // not excluded control characters
     \u0009 Horizontal tab
     \u000A Line feed
     \u000D Carriage return

     //valid, but discouraged
     \u0080 	<control>
     \u0081 	<control>
     \u0082 	BREAK PERMITTED HERE
     \u0083 	NO BREAK HERE
     \u0084 	<control>
     \u0085 	NEXT LINE (NEL)
     \u0086 	START OF SELECTED AREA
     \u0087 	END OF SELECTED AREA
     \u0088 	CHARACTER TABULATION SET
     \u0089 	CHARACTER TABULATION WITH JUSTIFICATION
     \u008A 	LINE TABULATION SET
     \u008B 	PARTIAL LINE FORWARD
     \u008C 	PARTIAL LINE BACKWARD
     \u008D 	REVERSE LINE FEED
     \u008E 	SINGLE SHIFT TWO
     \u008F 	SINGLE SHIFT THREE
     \u0090 	DEVICE CONTROL STRING
     \u0091 	PRIVATE USE ONE
     \u0092 	PRIVATE USE TWO
     \u0093 	SET TRANSMIT STATE
     \u0094 	CANCEL CHARACTER
     \u0095 	MESSAGE WAITING
     \u0096 	START OF GUARDED AREA
     \u0097 	END OF GUARDED AREA
     \u0098 	START OF STRING
     \u0099 	<control>
     \u009A 	SINGLE CHARACTER INTRODUCER
     \u009B 	CONTROL SEQUENCE INTRODUCER
     \u009C 	STRING TERMINATOR
     \u009D 	OPERATING SYSTEM COMMAND
     \u009E 	PRIVACY MESSAGE
     \u009F 	APPLICATION PROGRAM COMMAND
     */
    private static boolean isIllegal(char c) {

        switch (c) {
            case '\u0000': return true;
            case '\u0001': return true;
            case '\u0002': return true;
            case '\u0003': return true;
            case '\u0004': return true;
            case '\u0005': return true;
            case '\u0006': return true;
            case '\u0007': return true;
            case '\u0008': return true;
            case '\u000B': return true;
            case '\u000C': return true;
            case '\u000E': return true;
            case '\u000F': return true;
            case '\u0010': return true;
            case '\u0011': return true;
            case '\u0012': return true;
            case '\u0013': return true;
            case '\u0014': return true;
            case '\u0015': return true;
            case '\u0016': return true;
            case '\u0017': return true;
            case '\u0018': return true;
            case '\u0019': return true;
            case '\u001A': return true;
            case '\u001B': return true;
            case '\u001C': return true;
            case '\u001D': return true;
            case '\u001E': return true;
            case '\u001F': return true;
            case '\u0080': return true;
            case '\u0081': return true;
            case '\u0082': return true;
            case '\u0083': return true;
            case '\u0084': return true;
            case '\u0085': return true;
            case '\u0086': return true;
            case '\u0087': return true;
            case '\u0088': return true;
            case '\u0089': return true;
            case '\u008A': return true;
            case '\u008B': return true;
            case '\u008C': return true;
            case '\u008D': return true;
            case '\u008E': return true;
            case '\u008F': return true;
            case '\u0090': return true;
            case '\u0091': return true;
            case '\u0092': return true;
            case '\u0093': return true;
            case '\u0094': return true;
            case '\u0095': return true;
            case '\u0096': return true;
            case '\u0097': return true;
            case '\u0098': return true;
            case '\u0099': return true;
            case '\u009A': return true;
            case '\u009B': return true;
            case '\u009C': return true;
            case '\u009D': return true;
            case '\u009E': return true;
            case '\u009F': return true;
            default: return false;
        }

    }

    /**
     * Substitutes all illegal characters in the given string by the value of
     * {@link EscapingXMLUtilities#substitute}. If no illegal characters
     * were found, no copy is made and the given string is returned.
     *
     * @param string
     * @return
     */
    public static String escapeCharacters(String string) {

        char[] copy = null;
        boolean copied = false;
        for (int i = 0; i < string.length(); i++) {
            if (isIllegal(string.charAt(i))) {
                if (!copied) {
                    copy = string.toCharArray();
                    copied = true;
                }
                copy[i] = substitute;
            }
        }
        return copied ? new String(copy) : string;
    }

}
