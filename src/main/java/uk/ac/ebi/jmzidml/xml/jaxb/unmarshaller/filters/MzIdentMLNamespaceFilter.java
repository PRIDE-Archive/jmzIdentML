/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package uk.ac.ebi.jmzidml.xml.jaxb.unmarshaller.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

import uk.ac.ebi.jmzidml.model.utils.MzIdentMLVersion;

/**
 * @author Ritesh
 */
public class MzIdentMLNamespaceFilter extends XMLFilterImpl {


    private static final Logger logger = LoggerFactory.getLogger(MzIdentMLNamespaceFilter.class);
    private MzIdentMLVersion mzidVer = MzIdentMLVersion.Version_1_1;  // default vesion is mzIdentML 1.1

    //private static final Logger logger = LoggerFactory.getLogger(MzIdentMLNamespaceFilter.class);

    public MzIdentMLNamespaceFilter() {
        logger.debug("MzMLNamespaceFilter created. Remember to call setParent(XMLReader)");
    }
    
    public MzIdentMLNamespaceFilter(XMLReader reader) {
        super(reader);
    }
    
    public MzIdentMLNamespaceFilter(MzIdentMLVersion ver) {
        if (ver instanceof MzIdentMLVersion){
            this.mzidVer = ver;
        }
    }
    
    public MzIdentMLVersion getMzIdentMLVersion() {
        return mzidVer;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        // the elements are defined by a qualified schema, but we rip them out of context with the xxindex
        // so the namespace information is lost and we have to add it again here manually
        logger.trace("Changing namespace. uri: " + uri + " \tlocalName: " + localName + " \tqName: " + qName + " \tatts: " + atts);
        if (uri.length() == 0){
            super.startElement(this.mzidVer.getNameSpace(), localName, qName, atts);
        }
        else super.startElement(uri, localName, qName, atts);
    }
    
}
