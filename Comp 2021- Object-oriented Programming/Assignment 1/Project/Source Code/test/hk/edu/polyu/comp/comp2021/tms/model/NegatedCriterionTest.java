package hk.edu.polyu.comp.comp2021.tms.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This class represents a test suite for the NegatedCriterion class.
 * It contains methods to test all the functionalities provided by the NegatedCriterion class.
 * Each method in this class represents a test case for a specific functionality in the NegatedCriterion class.
 *
 * @author liuyuyang
 * @version 1.0
 */
public class NegatedCriterionTest {

    private Criterion subCriterion;
    private NegatedCriterion negatedCriterion;

    /**
     * Setup method is run before each test and initializes the necessary objects for testing.
     */
    @Before
    public void setUp() {
        subCriterion = new Criterion("a", "b", "c", new Object(), null, null, "d", "e");
        negatedCriterion = new NegatedCriterion("NegatedCriterion", subCriterion);
    }

    /**
     * Tests the getName method of the NegatedCriterion class.
     */
    @Test
    public void testGetName() {
        assertEquals("NegatedCriterion", negatedCriterion.getName());
    }

    /**
     * Tests the getFirstSubCriterion method of the NegatedCriterion class.
     */
    @Test
    public void testGetFirstSubCriterion() {
        assertEquals(subCriterion, negatedCriterion.getFirstSubCriterion());
    }

    /**
     * Tests the getSecondSubCriterion method of the NegatedCriterion class.
     * Since the second sub-criterion of a negated criterion is null, this method should always return null.
     */
    @Test
    public void testGetSecondSubCriterion() {
        assertEquals(null, negatedCriterion.getSecondSubCriterion());
    }
}
