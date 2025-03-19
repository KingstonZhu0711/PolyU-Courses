package hk.edu.polyu.comp.comp2021.tms.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class represents a test suite for the PrimitiveTask class.
 * It contains methods to test all the functionalities provided by the PrimitiveTask class.
 * Each method in this class represents a test case for a specific functionality in the PrimitiveTask class.
 *
 * @author liuyuyang
 * @version 1.0
 */
public class PrimitiveTaskTest {

    private Task[] directPrerequisites;
    private PrimitiveTask primitiveTask;

    /**
     * Setup method is run before each test and initializes the necessary objects for testing.
     */
    @Before
    public void setUp() {
        directPrerequisites = new Task[] {new PrimitiveTask("Prerequisite1", "Description1", 1.0, new Task[0]),
                new PrimitiveTask("Prerequisite2", "Description2", 2.0, new Task[0])};
        primitiveTask = new PrimitiveTask("PrimitiveTask", "Description", 3.0, directPrerequisites);
    }

    /**
     * Tests the getName method of the PrimitiveTask class.
     */
    @Test
    public void testGetName() {
        assertEquals("PrimitiveTask", primitiveTask.getName());
    }

    /**
     * Tests the getDescription method of the PrimitiveTask class.
     */
    @Test
    public void testGetDescription() {
        assertEquals("Description", primitiveTask.getDescription());
    }

    /**
     * Tests the getDuration method of the PrimitiveTask class.
     */
    @Test
    public void testGetDuration() {
        assertEquals(3.0, primitiveTask.getDuration(), 0.00001);
    }

    /**
     * Tests the getDirectPrerequisites method of the PrimitiveTask class.
     */
    @Test
    public void testGetDirectPrerequisites() {
        Task[] retrievedPrerequisites = primitiveTask.getDirectPrerequisites();
        assertEquals(directPrerequisites.length, retrievedPrerequisites.length);
        for (int i = 0; i < directPrerequisites.length; i++) {
            assertEquals(directPrerequisites[i], retrievedPrerequisites[i]);
        }
    }

}