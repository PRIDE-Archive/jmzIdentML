package uk.ac.ebi.jmzidml.test.xml;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import uk.ac.ebi.jmzidml.MzIdentMLElement;
import uk.ac.ebi.jmzidml.model.mzidml.*;
import uk.ac.ebi.jmzidml.xml.io.MzIdentMLUnmarshaller;

import java.net.URL;
import java.util.List;

/**
 * Package  : uk.ac.ebi.jmzidml.test.xml
 * Author   : riteshk
 * Date     : Sep 18, 2010
 */
public class SequenceCollectionTest extends TestCase {

    Logger log = Logger.getLogger(SequenceCollectionTest.class);

    public void testSequenceCollectionInformation() throws Exception {
        log.info("testing <SequenceCollection> content.");

        URL xmlFileURL = SequenceCollectionTest.class.getClassLoader().getResource("Mascot_MSMS_example.mzid");
        assertNotNull(xmlFileURL);

        MzIdentMLUnmarshaller unmarshaller = new MzIdentMLUnmarshaller(xmlFileURL, true);
        assertNotNull(unmarshaller);

        // Number of Sequence collection
        int totalSequenceCollection = unmarshaller.getObjectCountForXpath(MzIdentMLElement.SequenceCollection.getXpath());
        assertEquals(1, totalSequenceCollection);

        SequenceCollection sc = unmarshaller.unmarshal(SequenceCollection.class);
        assertNotNull(sc);


        // look at the DBSequences
        List<DBSequence> dbsequence  = sc.getDBSequence();
        assertEquals(46, dbsequence.size());

        // check one DBSequence
        DBSequence dbseq = dbsequence.get(0);
        assertNotNull(dbseq.getAccession());
        if (MzIdentMLElement.DBSequence.isAutoRefResolving() && dbseq.getSearchDatabaseRef() != null) {
            assertNotNull(dbseq.getSearchDatabase());
            assertEquals("SwissProt", dbseq.getSearchDatabase().getName());
            log.debug("DBSequence Acc:" + dbseq.getAccession()
                    + "Id:" + dbseq.getId()
                    + "Name:" + dbseq.getName()
                    + "Database name:" + dbseq.getSearchDatabase().getName()
                    + "length:" + dbseq.getLength());
        } else {
            System.out.println("DBSequence is not auto-resolving or does not contain a SearchDatabase reference");
            assertNull(dbseq.getSearchDatabase());
        }

        assertEquals(dbseq.getLength().intValue(), dbseq.getSeq().length());

        assertEquals(3, dbseq.getParamGroup().size());
        for (AbstractParam param : dbseq.getParamGroup()) {
            log.debug("Param:" + param.getName()
                    + " acc:" + param.getUnitAccession()
                    + " value:" + param.getValue()
                    + " unitCvRef:" + param.getUnitCvRef());
        }


        // look at the Peptides
        List<Peptide> peptides = sc.getPeptide();
        assertEquals(40, peptides.size());
        for (Peptide pep : peptides) {
            assertTrue(pep.getId().startsWith("peptide_"));
        }

        // check modifications of first peptide
        List<Modification> mods = peptides.get(0).getModification();
        assertEquals(1, mods.size());
        Modification mod = mods.get(0);
        assertTrue(mod.getMonoisotopicMassDelta() > 1);
        assertEquals(1, mod.getCvParam().size());
        assertTrue(mod.getCvParam().get(0) instanceof CvParam);
        CvParam cvparam = (CvParam) mod.getCvParam().get(0);
        assertTrue(cvparam.getAccession().startsWith("UNIMOD:"));

        // look at the PeptideEvidences
        List<PeptideEvidence> evidenceList = sc.getPeptideEvidence();
        assertEquals(56, evidenceList.size());

        // get the first PeptideEvidence and check if the auto-resolving works (if enabled)
        PeptideEvidence evidence = evidenceList.get(0);
        assertNotNull(evidence);
        // check the DBSequenceRef
        String dbSequenceRef = evidence.getDBSequenceRef();
        assertNotNull(dbSequenceRef);
        if (MzIdentMLElement.PeptideEvidence.isAutoRefResolving() && evidence.getDBSequenceRef() != null) {
            DBSequence sequence = evidence.getDBSequence();
            assertNotNull(sequence);
            assertEquals(dbSequenceRef, sequence.getId());
        } else {
            System.out.println("PeptideEvidence is not auto-resolving or does not contain a DBSequence reference");
            assertNull(evidence.getDBSequence());
        }
        // check the PeptideRef
        String peptideRef = evidence.getPeptideRef();
        assertNotNull(peptideRef);
        if (MzIdentMLElement.PeptideEvidence.isAutoRefResolving() && evidence.getPeptideRef() != null) {
            Peptide peptide = evidence.getPeptide();
            assertNotNull(peptide);
            assertEquals(peptideRef, peptide.getId());
        } else {
            System.out.println("PeptideEvidence is not auto-resolving or does not contain a Peptide reference");
            assertNull(evidence.getPeptide());
        }
        // check the PeptideRef
        String translationTableRef = evidence.getTranslationTableRef();
        if (MzIdentMLElement.PeptideEvidence.isAutoRefResolving() && evidence.getTranslationTableRef() != null) {
            TranslationTable translationTable = evidence.getTranslationTable();
            assertNotNull(translationTable);
            assertEquals(translationTableRef, translationTable.getId());
        } else {
            System.out.println("PeptideEvidence is not auto-resolving or does not contain a TranslationTable reference");
            assertNull(evidence.getTranslationTable());
        }


    }
}
