package hk.edu.polyu.comp.comp2021.tms.model;

// Import necessary classes from JUnit library for tests and assertions
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class represents a test suite for the BasicCriterion class.
 * It contains methods to test all the functionalities provided by the BasicCriterion class.
 *
 * @author liuyuyang
 */
public class BasicCriterionTest {

    /**
     * This test case checks the constructor of BasicCriterion class.
     * It creates an instance of BasicCriterion with predefined values and then checks if the values stored in the
     * object match the predefined values.
     */
    @Test
    public void testConstructor() {
        // Predefined values for testing
        String name = "testName";
        String property = "testProperty";
        String op = "testOp";
        Object value = "testValue";

        /**
         * Create a new instance of BasicCriterion with predefined values
         */

        BasicCriterion basicCriterion = new BasicCriterion(name, property, op, value);

        /**
         *  Assert that the values stored in the object match the predefined values
         */

        assertEquals(name, basicCriterion.getName());
        assertEquals(property, basicCriterion.getProperty());
        assertEquals(op, basicCriterion.getOp());
        assertEquals(value, basicCriterion.getValue());

        /**
         * Assert that the type of the criterion is "Basic"
         */
        assertEquals("Basic", basicCriterion.getType());
    }
}
