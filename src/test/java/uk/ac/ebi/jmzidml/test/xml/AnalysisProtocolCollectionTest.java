package uk.ac.ebi.jmzidml.test.xml;

import junit.framework.TestCase;
import org.slf4j.LoggerFactory;
import uk.ac.ebi.jmzidml.MzIdentMLElement;
import uk.ac.ebi.jmzidml.model.mzidml.AnalysisProtocolCollection;
import uk.ac.ebi.jmzidml.model.mzidml.Enzyme;
import uk.ac.ebi.jmzidml.model.mzidml.ProteinDetectionProtocol;
import uk.ac.ebi.jmzidml.model.mzidml.SpectrumIdentificationProtocol;
import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;

import java.net.URL;
import java.util.Iterator;

/**
 * Package  : uk.ac.ebi.jmzidml.test.xml
 * Author   : riteshk
 * Date     : Sep 18, 2010
 */
public class AnalysisProtocolCollectionTest extends TestCase {


    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(AnalysisProtocolCollectionTest.class);


    public void testAnalysisProtocolCollectionInformation() throws Exception {

        URL xmlFileURL = AnalysisProtocolCollectionTest.class.getClassLoader().getResource("Mascot_MSMS_example.mzid");
        assertNotNull(xmlFileURL);

        MzIdentMLUnmarshaller unmarshaller = new MzIdentMLUnmarshaller(xmlFileURL, true);
        assertNotNull(unmarshaller);

        AnalysisProtocolCollection apc =  unmarshaller.unmarshal(AnalysisProtocolCollection.class);
        assertNotNull("AnalysisProtocolCollection can not be null.", apc);

        ProteinDetectionProtocol pdp = apc.getProteinDetectionProtocol();
        assertNotNull("ProteinDetectionProtocol can not be null.", pdp);
        if (MzIdentMLElement.ProteinDetectionProtocol.isAutoRefResolving() && pdp.getAnalysisSoftwareRef() != null) {
            assertNotNull(pdp.getAnalysisSoftware());
            assertEquals("Mascot Parser", pdp.getAnalysisSoftware().getName());
        } else {
            System.out.println("ProteinDetectionProtocol is not auto-resolving or does not contain a AnalysisSoftware reference.");
            assertNull(pdp.getAnalysisSoftware());
        }

        Iterator<SpectrumIdentificationProtocol> sip = apc.getSpectrumIdentificationProtocol().iterator();
        assertNotNull("SpectrumIdentificationProtocol can not be null.", sip);
        Enzyme enzyme = sip.next().getEnzymes().getEnzyme().iterator().next();
        assertNotNull("Enzyme can not be null.", enzyme);
        logger.debug("Enzyme Id :" + enzyme.getId());
        assertEquals("ENZ_0", enzyme.getId());

        String siteRegex = enzyme.getSiteRegexp();
        logger.debug("Enzyme SiteRegex: " + siteRegex);
        assertNotNull(siteRegex);

    }

}
