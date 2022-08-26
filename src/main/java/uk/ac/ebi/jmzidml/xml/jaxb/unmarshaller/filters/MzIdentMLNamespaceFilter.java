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

import javax.xml.bind.annotation.XmlSchema;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author Ritesh
 */
public class MzIdentMLNamespaceFilter extends XMLFilterImpl {


    private static final Logger logger = LoggerFactory.getLogger(MzIdentMLNamespaceFilter.class);
    public static final String NAMESPACE = "namespace";
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

    /**
     * This method is used to change the JAXB binding of model classes from mzid version 1.1 to 1.2
     * @param namespace
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public static void changeNamespaceBinding(String namespace) throws IllegalAccessException, ClassNotFoundException {
        Annotation annotation = ClassLoader.getSystemClassLoader()
                .loadClass("uk.ac.ebi.jmzidml.model.mzidml.package-info").getAnnotation(XmlSchema.class);
        Object handler = Proxy.getInvocationHandler(annotation);
        Field f;
        try {
            f = handler.getClass().getDeclaredField("memberValues");
        } catch (NoSuchFieldException | SecurityException e) {
            throw new IllegalStateException(e);
        }
        f.setAccessible(true);
        ((Map<String, Object>)f.get(handler)).put(NAMESPACE,namespace);
    }

}
