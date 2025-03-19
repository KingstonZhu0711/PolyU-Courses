package hk.edu.polyu.comp.comp2021.tms.model;

import hk.edu.polyu.comp.comp2021.tms.model.Criterion;
import org.junit.Assert;
import org.junit.Test;

/**
 * This class represents a test suite for the Criterion class.
 * It contains methods to test all the functionalities provided by the Criterion class.
 *
 * @author liuyuyang
 */
public class CriterionTest {
    /**
     * This test case checks whether the CriterionName is valid or not.
     */
    @Test
    public void testValidCriterionName() {
        Assert.assertTrue(Criterion.isValidCriterionName("Test"));
        Assert.assertTrue(Criterion.isValidCriterionName("Test123"));
        Assert.assertFalse(Criterion.isValidCriterionName("123Test"));
        Assert.assertFalse(Criterion.isValidCriterionName("TestNameThatIsWayTooLong"));
        Assert.assertFalse(Criterion.isValidCriterionName("Invalid!"));
    }

    /**
     * This test case checks whether the CriterionProperty is valid or not.
     */
    @Test
    public void testValidCriterionProperty() {
        Assert.assertTrue(Criterion.isValidCriterionProperty("name"));
        Assert.assertTrue(Criterion.isValidCriterionProperty("description"));
        Assert.assertTrue(Criterion.isValidCriterionProperty("duration"));
        Assert.assertTrue(Criterion.isValidCriterionProperty("prerequisites"));
        Assert.assertTrue(Criterion.isValidCriterionProperty("subtasks"));
        Assert.assertFalse(Criterion.isValidCriterionProperty("invalid"));
    }

    /**
     * This test case checks whether the CriterionOp is valid or not.
     */
    @Test
    public void testValidCriterionOp() {
        Assert.assertTrue(Criterion.isValidCriterionOp("name", "contains"));
        Assert.assertTrue(Criterion.isValidCriterionOp("description", "contains"));
        Assert.assertTrue(Criterion.isValidCriterionOp("duration", ">"));
        Assert.assertTrue(Criterion.isValidCriterionOp("duration", "<"));
        Assert.assertTrue(Criterion.isValidCriterionOp("duration", ">="));
        Assert.assertTrue(Criterion.isValidCriterionOp("duration", "<="));
        Assert.assertTrue(Criterion.isValidCriterionOp("duration", "=="));
        Assert.assertTrue(Criterion.isValidCriterionOp("duration", "!="));
        Assert.assertTrue(Criterion.isValidCriterionOp("prerequisites", "contains"));
        Assert.assertTrue(Criterion.isValidCriterionOp("subtasks", "contains"));
        Assert.assertFalse(Criterion.isValidCriterionOp("name", "invalid"));
        Assert.assertFalse(Criterion.isValidCriterionOp("invalid", "contains"));
    }

    /**
     * This test case checks whether the CriterionValue is valid or not.
     */
    @Test
    public void testValidCriterionValue() {
        Assert.assertTrue(Criterion.isValidCriterionValue("name", "contains", "\"value\""));
        Assert.assertTrue(Criterion.isValidCriterionValue("description", "contains", "\"value\""));
        Assert.assertTrue(Criterion.isValidCriterionValue("duration", "<", "2"));
        Assert.assertTrue(Criterion.isValidCriterionValue("prerequisites", "contains", "\"value,value2\""));
        Assert.assertTrue(Criterion.isValidCriterionValue("subtasks", "contains", "\"value,value2\""));
        Assert.assertFalse(Criterion.isValidCriterionValue("duration", "<=", "invalid"));
        Assert.assertFalse(Criterion.isValidCriterionValue("invalid", "contains", "\"value\""));
    }

    /**
     * This test case checks the validity of CriterionValue in special cases.
     */
    @Test
    public void testValidCriterionValueSpecialCases() {
        Assert.assertFalse(Criterion.isValidCriterionValue("name", "contains", "value"));
        Assert.assertFalse(Criterion.isValidCriterionValue("description", "contains", "value"));

        Assert.assertFalse(Criterion.isValidCriterionValue("prerequisites", "contains", "\"value\""));
        Assert.assertFalse(Criterion.isValidCriterionValue("subtasks", "contains", "\"value\""));

        Assert.assertFalse(Criterion.isValidCriterionValue("name", ">", "\"value\""));
        Assert.assertFalse(Criterion.isValidCriterionValue("description", "<", "\"value\""));

        Assert.assertFalse(Criterion.isValidCriterionValue("duration", "contains", "2"));
        Assert.assertFalse(Criterion.isValidCriterionValue("duration", "invalidOp", "2"));

        Assert.assertFalse(Criterion.isValidCriterionValue("prerequisites", "contains", 123));
        Assert.assertFalse(Criterion.isValidCriterionValue("subtasks", "contains", 123));
        Assert.assertFalse(Criterion.isValidCriterionValue("prerequisites", "contains", null));
        Assert.assertFalse(Criterion.isValidCriterionValue("subtasks", "contains", null));
    }

    /**
     * This test case for Criterion does not cover all aspects if run alone. However, when run with the entire test suite&#xFF0C;
     * it can achieve a high coverage rate.
     */
}