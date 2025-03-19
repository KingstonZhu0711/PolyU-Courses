package hk.edu.polyu.comp.comp2021.tms.model;

// Import necessary classes from the project and JUnit library for tests and assertions
import hk.edu.polyu.comp.comp2021.tms.model.Criterion;
import hk.edu.polyu.comp.comp2021.tms.model.CriterionRecorder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * This class represents a test suite for the CriterionRecorder class.
 * It contains methods to test all the functionalities provided by the CriterionRecorder class.
 *
 * @author liuyuyang
 */
public class CriterionRecorderTest {

    private CriterionRecorder recorder;
    private Criterion criterion1;
    private Criterion criterion2;

    /**
     * This method sets up the CriterionRecorder and Criteria for the tests.
     */
    @Before
    public void setup() {
        // Set up the CriterionRecorder
        recorder = new CriterionRecorder();

        // Set up two Criteria
        criterion1 = new Criterion("Criterion1", "name", "contains", "\"value\"", null, null, "", "basic");
        criterion2 = new Criterion("Criterion2", "description", "==", "\"test\"", null, null, "", "basic");

        // Add the Criteria to the CriterionRecorder
        recorder.addCriterion(criterion1);
        recorder.addCriterion(criterion2);
    }

    /**
     * This test case checks the functionality of addCriterion method of CriterionRecorder class.
     */
    @Test
    public void testAddCriterion() {
        // Add a new Criterion to the CriterionRecorder
        Criterion criterion3 = new Criterion("Criterion3", "duration", "<", "2", null, null, "", "basic");
        recorder.addCriterion(criterion3);

        // Test if the new Criterion is added successfully
        Assert.assertEquals(recorder.getCriterion("Criterion3"), criterion3);
    }

    /**
     * This test case checks the functionality of deleteCriterion method of CriterionRecorder class.
     */
    @Test
    public void testDeleteCriterion() {
        // Delete a Criterion from the CriterionRecorder
        recorder.deleteCriterion("Criterion1");

        // Test if the Criterion is deleted successfully
        Assert.assertFalse(recorder.isInRecorder("Criterion1"));
    }

    /**
     * This test case checks the functionality of isInRecorder method of CriterionRecorder class.
     */
    @Test
    public void testIsInRecorder() {
        // Test if the Criterion is in the CriterionRecorder
        Assert.assertTrue(recorder.isInRecorder("Criterion1"));

        // Test if a non-existent Criterion is not in the CriterionRecorder
        Assert.assertFalse(recorder.isInRecorder("NonExistentCriterion"));
    }

    /**
     * This test case checks the functionality of getAllCriteria method of CriterionRecorder class.
     */
    @Test
    public void testGetAllCriteria() {
        // Get all Criteria from the CriterionRecorder
        Criterion[] allCriteria = recorder.getAllCriteria();

        // Test if the correct number of Criteria are returned
        Assert.assertEquals(2, allCriteria.length);

        // Test if the correct Criteria are returned
        Assert.assertEquals(criterion1, allCriteria[0]);
        Assert.assertEquals(criterion2, allCriteria[1]);
    }

    /**
     * This test case checks the functionality of getCriterion method of CriterionRecorder class.
     */
    @Test
    public void testGetCriterion() {
        // Test if the correct Criterion is returned
        Assert.assertEquals(criterion1, recorder.getCriterion("Criterion1"));

        // Test if null is returned for a non-existent Criterion
        Assert.assertNull(recorder.getCriterion("NonExistentCriterion"));
    }

    /**
     * This test case checks the functionality of showCriterion method of CriterionRecorder class.
     * It redirects the standard output to a ByteArrayOutputStream to check the output of the method.
     */
    @Test
    public void testShowCriterion() {
        // Redirect the standard output to a ByteArrayOutputStream
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Call the showCriterion method
        recorder.showCriterion("Criterion1");

        // Define the expected output
        String expectedOutput = "\r\n--------Information for Criterion1--------"
                + "\r\nCriterion Name: Criterion1"
                + "\r\nProperty: name"
                + "\r\nOp: contains"
                + "\r\nValue: \"value\"\r\n";

        // Test if the output matches the expected output
        Assert.assertEquals(expectedOutput, outContent.toString());
    }

    // Note: Different operating systems use different line separators. Windows uses "\r\n" while Mac and Linux use "\n".
    // The above tests may fail on Mac or Linux due to this difference. Please adjust the expected output accordingly.
}

