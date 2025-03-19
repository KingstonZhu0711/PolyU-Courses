package hk.edu.polyu.comp.comp2021.tms.model;

// Import necessary classes from JUnit library for tests and assertions
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class represents a test suite for the BinaryCriterion class.
 * It contains methods to test all the functionalities provided by the BinaryCriterion class.
 *
 * @author liuyuyang
 */
public class BinaryCriterionTest {

    /**
     * This test case checks the constructor of BinaryCriterion class.
     * It creates an instance of BinaryCriterion with predefined values and then checks if the values stored in the
     * object match the predefined values.
     */
    @Test
    public void testConstructor() {
        // Predefined values for testing
        String name = "testName";
        String logicOp = "AND";

        // Create two instances of BasicCriterion for testing
        BasicCriterion firstSubCriterion = new BasicCriterion("subName1", "subProperty1", "subOp1", "subValue1");
        BasicCriterion secondSubCriterion = new BasicCriterion("subName2", "subProperty2", "subOp2", "subValue2");

        // Create a new instance of BinaryCriterion with predefined values
        BinaryCriterion binaryCriterion = new BinaryCriterion(name, firstSubCriterion, secondSubCriterion, logicOp);

        // Assert that the values stored in the object match the predefined values
        assertEquals(name, binaryCriterion.getName());
        assertEquals(firstSubCriterion, binaryCriterion.getFirstSubCriterion());
        assertEquals(secondSubCriterion, binaryCriterion.getSecondSubCriterion());
        assertEquals(logicOp, binaryCriterion.getLogicOp());

        // Assert that the type of the criterion is "Binary"
        assertEquals("Binary", binaryCriterion.getType());
    }
}
