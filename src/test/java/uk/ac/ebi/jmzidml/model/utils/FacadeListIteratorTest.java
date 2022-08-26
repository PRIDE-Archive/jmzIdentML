package uk.ac.ebi.jmzidml.model.utils;

import org.junit.Before;
import org.junit.Test;
import uk.ac.ebi.jmzidml.model.mzidml.AbstractParam;
import uk.ac.ebi.jmzidml.model.mzidml.CvParam;
import uk.ac.ebi.jmzidml.model.mzidml.UserParam;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.*;

/**
 * Created by IntelliJ IDEA.
 * User: rwang
 * Date: 04/02/11
 * Time: 10:37
 * To change this template use File | Settings | File Templates.
 */
public class FacadeListIteratorTest {
    private FacadeList<CvParam> cvList;

    @Before
    public void setUp() throws Exception {
        List<AbstractParam> paramList = new ArrayList<>();

        CvParam cv = new CvParam();
        cv.setAccession("CV1");
        paramList.add(cv);

        CvParam cv1 = new CvParam();
        cv1.setAccession("CV2");
        paramList.add(cv1);

        UserParam user = new UserParam();
        user.setName("User1");
        paramList.add(user);

        UserParam user1 = new UserParam();
        user1.setName("User2");
        paramList.add(user1);

        CvParam cv2 = new CvParam();
        cv2.setAccession("CV3");
        paramList.add(cv2);

        CvParam cv3 = new CvParam();
        cv3.setAccession("CV4");
        paramList.add(cv3);

        UserParam user2 = new UserParam();
        user2.setName("User3");
        paramList.add(user2);

        cvList = new FacadeList<>(paramList, CvParam.class);
    }

    /**************************** Test hasNext() *********************************/

    /**
     * Confirm has next element
     *
     * @throws Exception
     */
    @Test
    public void testListIteratorHasNext() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        assertTrue(it.hasNext());
    }

    /**
     * Confirm has next element with sublist start index
     * @throws Exception
     */
    @Test
    public void testListIteratorHasNextWithIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(1);
        assertTrue(it.hasNext());
    }

    /**
     * Check IndexOutOfBoundsException when sublist start index over the limit
     * @throws Exception
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testListIteratorNotHasNextWithIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(4);
    }

    /****************************** Test next() ********************************/

    /**
     * Confirm sublist's next element is correct
     * @throws Exception
     */
    @Test
    public void testListIteratorNext() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        CvParam cv = it.next();
        System.out.println("CV Accession: " + cv.getAccession());
        assertEquals("CV1", cv.getAccession());
    }

    /**
     * Confirm sublist's next element using sublist start index
     * @throws Exception
     */
    @Test
    public void testListIteratorNextWithIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(1);
        CvParam cv = it.next();
        System.out.println("Cv Accession:" + cv.getAccession());
        assertEquals("CV2", cv.getAccession());
    }

    /**
     * Confirm the first/last element of the sublist is correct
     * using sublist start index
     * @throws Exception
     */
    @Test
    public void testListIteratorLastNextWithIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(3);
        CvParam cv = it.next();
        assertEquals("CV4", cv.getAccession());
    }


    /******************************* Tst hasPrevious() ****************************/

    /**
     * Confirm has previous returns correct properly.
     *
     * @throws Exception
     */
    @Test
    public void testListIteratorHasPrevious() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        it.next();
        assertTrue(it.hasPrevious());
    }

    /**
     * Confirm hasPrevious returns false if next not called at all.
     *
     * @throws Exception
     */
    @Test
    public void testListIteratorHasNoPrevious() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        assertFalse(it.hasPrevious());
    }

    /**
     * hasPrevious using start index. Confirm hasPrevious does not consider actual first element (CV1) as part
     * of the sublist.
     *
     * @throws Exception
     */
    @Test
    public void testListIteratorHasPreviousWithIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(1);
        assertFalse(it.hasPrevious());
        it.next();
        assertTrue(it.hasPrevious());
    }


    /** Tests for previous() */

    /**
     * Try to retrieve previous element before next has been called.
     *
     * @throws Exception
     */
    @Test(expected = NoSuchElementException.class)
    public void testPreviousFromStart() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        it.previous();
    }

    @Test
    public void testPrevious() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        it.next();
        CvParam cv = it.previous();
        assertNotNull(cv);
        assertEquals("CV1", cv.getAccession());
    }

    /**
     * Pass an index into listiterator call and call previous(). Should throw Exception
     *
     * @throws Exception
     */
    @Test(expected = NoSuchElementException.class)
    public void testPreviousFailingWithIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(1);
        it.previous();
    }


    /**
     * Tests for nextIndex()
     */
    @Test
    public void testNextIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        int nextIndex = it.nextIndex();
        assertEquals(0, nextIndex);
    }

    /**
     * Confirm first index is 0 when using start index.
     * @throws Exception
     */
    @Test
    public void testNextIndexWithIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(2);
        int nextIndex = it.nextIndex();
        assertEquals(0, nextIndex);
    }

    /**
     * Confirm first index is 0 when setting start index to end of list.
     * @throws Exception
     */
    @Test
    public void testNextIndexWithIndexEnd() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(3);
        int nextIndex = it.nextIndex();
        assertEquals(0, nextIndex);
    }

    /**
     * Set start index to last CV param and call next(). nextIndex() should return the size of the list when past the end of the list,
     * in this = 1.
     * @throws Exception
     */
    @Test
    public void testNextIndexWithIndexSize() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(3);
        it.next();
        int nextIndex = it.nextIndex();
        assertEquals(1, nextIndex);
    }


    /**
     * Tests for previousIndex()
     */
    @Test
    public void testPreviousIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        it.next();
        it.next();
        it.next();
        int previousIndex = it.previousIndex();
        assertEquals(2, previousIndex);
    }

    /**
     * Confirm -1 returned when previousIndex() called before next() called.
     * @throws Exception
     */
    @Test
    public void testPreviousIndexWithOutNext() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        int previousIndex = it.previousIndex();
        assertEquals(-1, previousIndex);
    }

    /**
     * Confirm previousIndex() eqauls 1 when using start index and next() called twice
     * @throws Exception
     */
    @Test
    public void testPreviousIndexWithIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(1);
        it.next();
        it.next();
        int previousIndex = it.previousIndex();
        assertEquals(1, previousIndex);
    }

    /**
     * Confirm -1 returned when previousIndex() called before next() called when using start index
     *
     * @throws Exception
     */
    @Test
    public void testPreviousIndexWithoutNext() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(1);
        int previousIndex = it.previousIndex();
        assertEquals(-1, previousIndex);
    }

    /**
     * When start index set to last element, call next() and confirm previousIndex() is 0.
     *
     * @throws Exception
     */
    @Test
    public void testPreviousIndexWithIndexLastElement() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(3);
        it.next();
        int previousIndex = it.previousIndex();
        assertEquals(0, previousIndex);
    }

    /**
     * Tests for remove()
     */

    /**
     * Remove first element from the list and confirm next() returns reference to CV2.
     */
    @Test
    public void testRemoveWithNext() {
        ListIterator<CvParam> it = cvList.listIterator();
        it.next();
        it.remove();
        assertEquals("CV2", it.next().getAccession());
    }

    /**
     * Confirm correct element returned when next(), previous() and remove() called
     *
     * @throws Exception
     */
    @Test
    public void testRemoveWithNextAndPrevious() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        it.next();
        it.previous();
        it.remove();
        assertEquals("CV2", it.next().getAccession());
    }

    /**
     * Remove first element from the list and confirm next() returns reference to CV3 (start index is set to 1).
     *
     * @throws Exception
     */
    @Test
    public void testRemoveWithIndexAndNext() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(1);
        it.next();
        it.remove();
        assertEquals("CV3", it.next().getAccession());
    }

    /**
     * Confirm correct element returned when next(), previous() and remove() called when using start index > 0.
     * @throws Exception
     */
    @Test
    public void testRemoveWithIndexAndPrevious() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(1);
        it.next();
        it.previous();
        it.remove();
        CvParam cv = it.next();
        assertEquals("CV3", cv.getAccession());
    }

    /**
     * Set start index > 0, use next() to iterate to end of sublist, remove() and confirm previous() return reference to CV3
     *
     * @throws Exception
     */
    @Test
    public void testRemoveWithIndexLastElement() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(1);
        it.next();
        it.next();
        it.next();
        it.remove();
        CvParam cv = it.previous();
        assertEquals("CV3", cv.getAccession());
    }

    /**
     * Set start index > 0, using next(), previous() and remove() and confirm next() returns reference to CV3
     *
     * @throws Exception
     */
    @Test
    public void testRemoveWithNextPrevious() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(1);
        it.next();
        it.previous();
        it.next();
        it.remove();
        CvParam cv = it.next();
        assertEquals("CV3", cv.getAccession());
    }

    /**
     * Use next() and remove() and confirm nextIndex still returns 0
     *
     * @throws Exception
     */
    @Test
    public void testRemoveWithNextIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        it.next();
        it.remove();
        assertEquals(0, it.nextIndex());
    }

    /**
     * Remove first element from list and confirm previousIndex returns -1.
     * @throws Exception
     */
    @Test
    public void testRemoveWithPreviousIndexFromStartOfList() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        it.next();
        it.remove();
        assertEquals(-1, it.previousIndex());
    }

    /**
     * Remove second element from list and confirm nextIndex() reset to 1 (pointing to new second element).
     * @throws Exception
     */
    @Test
    public void testRemoveWithTwoNextRemoveNextIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        it.next();
        it.next();
        it.remove();
        assertEquals(1, it.nextIndex());
    }

    /**
     * Remove second element from list and confirm previousIndex() reset to 0 (pointing to first element).
     * @throws Exception
     */
    @Test
    public void testRemoveWithTwoNextRemovePreviousIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        it.next();
        it.next();
        it.remove();
        assertEquals(0, it.previousIndex());
    }

    /**
     * Starting at start of list, iterate to last element and remove. Confirm nextIndex() set to 3
     * @throws Exception
     */
    @Test
    public void testRemoveAtEndOfListWithNextIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        it.next();
        it.next();
        it.next();
        it.next();
        it.remove();
        assertEquals(3, it.nextIndex());
    }

    /**
     * Starting at start of list, iterate to last element and remove. Confirm previousIndex set to 2
     * @throws Exception
     */
    @Test
    public void testRemoveAtEndOfListWithPreviousIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        it.next();
        it.next();
        it.next();
        it.next();
        it.remove();
        assertEquals(2, it.previousIndex());
    }

    /**
     * Set start index > 0 and remove first element. Confirm nextIndex = 0;
     * @throws Exception
     */
    @Test
    public void testRemoveNextIndexWithListIteratorStartIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(2);
        it.next();
        it.remove();
        assertEquals(0, it.nextIndex());
    }

    /**
     * Set start index > 0, remove first element and confirm previousIndex() is -1.
     * @throws Exception
     */
    @Test
    public void testRemovePreviousIndexWithListIteratorStartIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(2);
        it.next();
        it.remove();
        assertEquals(-1, it.previousIndex());
    }

    /**
     * ******************** Test adding elements *************************************
     */

    /**
     * Add a new element to the sublist
     *
     * @throws Exception
     */
    @Test
    public void testAddElement() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        CvParam cv = new CvParam();
        cv.setAccession("addedCV1");
        it.add(cv);
        cv = it.next();
        assertEquals("addedCV1", cv.getAccession());
    }

    /**
     * Confirm nextIndex after adding a element
     *
     * @throws Exception
     */
    @Test
    public void testAddElementWithNextIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        CvParam cv = new CvParam();
        cv.setAccession("addedCV1");
        it.add(cv);
        assertEquals(0, it.nextIndex());
    }

    /**
     * Confirm previousIndex after adding a element
     *
     * @throws Exception
     */
    @Test
    public void testAddElementWithPreviousIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        CvParam cv = new CvParam();
        cv.setAccession("addedCV1");
        it.add(cv);
        assertEquals(-1, it.previousIndex());
    }


    /**
     * Add a new element using sublist starting index
     *
     * @throws Exception
     */
    @Test
    public void testAddElementWithStartIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(2);
        CvParam cv = new CvParam();
        cv.setAccession("addedCV1");
        it.add(cv);
        cv = it.next();
        assertEquals("addedCV1", cv.getAccession());
    }

    /**
     * Confirm NoSuchElementException after setting the first element then calling previous()
     *
     * @throws Exception
     */
    @Test(expected = NoSuchElementException.class)
    public void testAddElementPrevious() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        CvParam cv = new CvParam();
        cv.setAccession("addedCV1");
        it.add(cv);
        it.previous();
    }

    /**
     * Confirm NoSuchElementException after setting the first element then calling previous()
     * using sublist start index
     *
     * @throws Exception
     */
    @Test(expected = NoSuchElementException.class)
    public void testAddElementPreviousAndIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(2);
        CvParam cv = new CvParam();
        cv.setAccession("addedCV1");
        it.add(cv);
        it.previous();
    }

    /**
     * Confirm nextIndex after adding a element using sublist start index
     *
     * @throws Exception
     */
    @Test
    public void testAddElementWithNextIndexAndStartIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(2);
        CvParam cv = new CvParam();
        cv.setAccession("addedCV1");
        it.add(cv);
        assertEquals(0, it.nextIndex());
    }

    /**
     * Confirm previousIndex after adding a element using sublist start index
     *
     * @throws Exception
     */
    @Test
    public void testAddElementWithPreviousIndexAndStartIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(2);
        CvParam cv = new CvParam();
        cv.setAccession("addedCV1");
        it.add(cv);
        assertEquals(-1, it.previousIndex());
    }

    /**
     * ********************* Test setting elements ************************************
     */

    /**
     * Test setting element
     *
     * @throws Exception
     */
    @Test
    public void testSetElement() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        it.next();
        CvParam cv = new CvParam();
        cv.setAccession("setCV1");
        it.set(cv);
        assertEquals("setCV1", it.previous().getAccession());
    }

    /**
     * Setting element has correctly replaced the element in current iterator index
     * And next() returns correct instance
     *
     * @throws Exception
     */
    @Test
    public void testSetElementCheckReplace() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        it.next();
        CvParam cv = new CvParam();
        cv.setAccession("setCV1");
        it.set(cv);
        assertEquals("CV2", it.next().getAccession());
    }

    /**
     * Setting element after previous() been called
     *
     * @throws Exception
     */
    @Test
    public void testSetElementWithPrevious() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        it.next();
        it.previous();
        CvParam cv = new CvParam();
        cv.setAccession("setCV1");
        it.set(cv);
        assertEquals("setCV1", it.next().getAccession());
    }


    /**
     * Setting element with specified sublist start index
     *
     * @throws Exception
     */
    @Test
    public void testSetElementWithPreviousAndStartIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(2);
        it.next();
        it.previous();
        CvParam cv = new CvParam();
        cv.setAccession("setCV1");
        it.set(cv);
        assertEquals("setCV1", it.next().getAccession());
        assertEquals("CV4", it.next().getAccession());
    }

    /**
     * Setting element after previous() has been called
     * Together with specified sublist start index
     *
     * @throws Exception
     */
    @Test
    public void testSetElementWithPreviousAndLastIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(2);
        it.next();
        it.next();
        CvParam cv = new CvParam();
        cv.setAccession("setCV1");
        it.set(cv);
        assertEquals("setCV1", it.previous().getAccession());
    }

    /**
     * hasNext() after setting the last element of the sublist
     *
     * @throws Exception
     */
    @Test
    public void testSetElementWithHasNext() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(2);
        it.next();
        it.next();
        CvParam cv = new CvParam();
        cv.setAccession("setCV1");
        it.set(cv);
        assertFalse(it.hasNext());
    }

    /**
     * hasPrevious() after setting the last element f the sublist
     *
     * @throws Exception
     */
    @Test
    public void testSetELementWithHasPrevious() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(2);
        it.next();
        it.next();
        CvParam cv = new CvParam();
        cv.setAccession("setCV1");
        it.set(cv);
        assertTrue(it.hasPrevious());
    }

    /**
     * Setting element with specified sublist starting index
     *
     * @throws Exception
     */
    @Test
    public void testSetElementWithStartIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(2);
        it.next();
        CvParam cv = new CvParam();
        cv.setAccession("setCV1");
        it.set(cv);
        assertEquals("setCV1", it.previous().getAccession());
    }

    /**
     * Setting element has correctly replaced the element in current iterator index
     * And next() returns correct instance, using specified sublist start index
     *
     * @throws Exception
     */
    @Test
    public void testSetElementCheckReplaceWithStartIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(2);
        it.next();
        CvParam cv = new CvParam();
        cv.setAccession("setCV1");
        it.set(cv);
        assertEquals("CV4", it.next().getAccession());
    }

    /**
     * nextIndex() after setting the first element of the sublist
     *
     * @throws Exception
     */
    @Test
    public void testSetElementWithNextIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        it.next();
        CvParam cv = new CvParam();
        cv.setAccession("setCV1");
        it.set(cv);
        assertEquals(1, it.nextIndex());
    }

    /**
     * previousIndex() after setting the first element of the sublist
     *
     * @throws Exception
     */
    @Test
    public void testSetElementWithPreviousIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        it.next();
        CvParam cv = new CvParam();
        cv.setAccession("setCV1");
        it.set(cv);
        assertEquals(0, it.previousIndex());
    }

    /**
     * nextIndex() after setting the first element of the sublist
     * Together with sublist starting index
     *
     * @throws Exception
     */
    @Test
    public void testSetElementWithNextIndexAndInputIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(2);
        it.next();
        CvParam cv = new CvParam();
        cv.setAccession("setCV1");
        it.set(cv);
        assertEquals(1, it.nextIndex());
    }

    /**
     * previousIndex() after setting the first element of the sublist
     * Together with sublist starting index
     *
     * @throws Exception
     */
    @Test
    public void testSetElementWithPreviousIndexAndInputIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(2);
        it.next();
        CvParam cv = new CvParam();
        cv.setAccession("setCV1");
        it.set(cv);
        assertEquals(0, it.previousIndex());
    }

    /**
     * Setting the last element of the sublist
     * Together with sublist starting index
     *
     * @throws Exception
     */
    @Test
    public void testSetLastElementWithInputIndex() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator(2);
        it.next();
        it.next();
        CvParam cv = new CvParam();
        cv.setAccession("setCV1");
        it.set(cv);
        assertEquals("setCV1", it.previous().getAccession());
    }

    /**
     * Setting element after remove() has been called
     * This should produce an IllegalStateException
     *
     * @throws Exception
     */
    @Test(expected = IllegalStateException.class)
    public void testSetElementIllegalRemove() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        it.next();
        it.remove();
        CvParam cv = new CvParam();
        cv.setAccession("setCV1");
        it.set(cv);
    }

    /**
     * Setting element after remove() then next() methods have been called
     *
     * @throws Exception
     */
    @Test
    public void testSetElementRemove() throws Exception {
        ListIterator<CvParam> it = cvList.listIterator();
        it.next();
        it.next();
        it.remove();
        it.next();
        CvParam cv = new CvParam();
        cv.setAccession("setCV1");
        it.set(cv);
        cv = it.next();
        assertEquals("CV4", cv.getAccession());
    }

    /**
     * ******************** Test for empty super list *********************************
     */

    @Test
    public void testEmptySuperListHasNext() throws Exception {
        List superlist = new ArrayList();
        FacadeList list = new FacadeList(superlist, CvParam.class);
        ListIterator it = list.listIterator();
        assertFalse(it.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testEmptySuperListNext() throws Exception {
        List superlist = new ArrayList();
        FacadeList list = new FacadeList(superlist, CvParam.class);
        ListIterator it = list.listIterator();
        it.next();
    }

    @Test
    public void testEmptySuperListHasPrevious() throws Exception {
        List superlist = new ArrayList();
        FacadeList list = new FacadeList(superlist, CvParam.class);
        ListIterator it = list.listIterator();
        assertFalse(it.hasPrevious());
    }

    @Test(expected = NoSuchElementException.class)
    public void testEmptySuperListPrevious() throws Exception {
        List superlist = new ArrayList();
        FacadeList list = new FacadeList(superlist, CvParam.class);
        ListIterator it = list.listIterator();
        it.previous();
    }

    @Test
    public void testEmptySuperListNextIndex() throws Exception {
        List superlist = new ArrayList();
        FacadeList list = new FacadeList(superlist, CvParam.class);
        ListIterator it = list.listIterator();
        int nextIndex = it.nextIndex();
        assertEquals(0, nextIndex);
    }

    @Test
    public void testEmptySuperListPreviousIndex() throws Exception {
        List superlist = new ArrayList();
        FacadeList list = new FacadeList(superlist, CvParam.class);
        ListIterator it = list.listIterator();
        int previousIndex = it.previousIndex();
        assertEquals(-1, previousIndex);
    }
/*
    @Test
    public void testNextPositionatminusone() {
        List list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");

        ListIterator listIt = list.listIterator();
        listIt.next();
        if (listIt.hasPrevious()) {
            System.out.println("hasprevious");
            String value = (String) listIt.previous();
            System.out.println("value " + value);
        }

    }

    @Test
    public void testNextPositionAtZero() {
        List list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");

        ListIterator listIt = list.listIterator();
        listIt.next();
        int previous = listIt.previousIndex();
        System.out.println(previous);

    }

    @Test
    public void testNextWithPrevious() throws Exception {
        List list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");

        ListIterator listIt = list.listIterator();
        listIt.next();
        String next = (String) listIt.next();
        System.out.println("Next: " + next);
        String previous = (String) listIt.previous();
        System.out.println("Previous: " + previous);
    }

    @Test
    public void testNextWithRemove() throws Exception {
        List list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");

        ListIterator listIt = list.listIterator();
        listIt.next();
        listIt.next();
        listIt.previous();
        listIt.remove();
        System.out.println(listIt.next().toString());
    }
           */

    /**
     * test multiple removes
     */
    public void testMultipleRemoves() throws Exception{
        fail();
    }
}
