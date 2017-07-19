package uk.ac.ebi.jmzidml.test.xml;

import junit.framework.TestCase;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.jmzidml.model.mzidml.BibliographicReference;
import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;

import java.net.URL;

/**
 * Package  : uk.ac.ebi.jmzidml.test.xml
 * Author   : riteshk
 * Date     : Sep 19, 2010
 */
public class BibliographicTest extends TestCase {

    org.slf4j.Logger logger = LoggerFactory.getLogger(BibliographicTest.class);


    public void testBibliographicInformation() throws Exception {

        URL xmlFileURL = BibliographicTest.class.getClassLoader().getResource("Mascot_MSMS_example.mzid");
        assertNotNull(xmlFileURL);

        MzIdentMLUnmarshaller unmarshaller = new MzIdentMLUnmarshaller(xmlFileURL, true);
        assertNotNull(unmarshaller);

        logger.debug("unmarshalling BibliographicReference and checking content.");
        BibliographicReference bib =  unmarshaller.unmarshal(BibliographicReference.class);
        assertNotNull(bib);

        assertEquals("Wiley VCH", bib.getPublisher());
        assertEquals("3551-3567", bib.getPages());
        assertEquals("Electrophoresis", bib.getPublication());
        assertEquals(1999, bib.getYear().intValue());

    }

}
