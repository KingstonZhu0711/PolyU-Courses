package hk.edu.polyu.comp.comp2021.tms.model;

import hk.edu.polyu.comp.comp2021.tms.model.instructions.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.*;

/**
 * This class represents a test suite for the InstructionEnumeration class.
 * It contains methods to test all the functionalities provided by the InstructionEnumeration class.
 *
 * @author liuyuyang
 */
public class InstructionEnumerationTest {

    private TaskRecorder taskRecorder;
    private HistoryCommandRecorder commandRecorder;
    private CriterionRecorder criterionRecorder;
    private String[] parameters;

    /**
     * Setup method is run before each test and initializes the necessary objects for testing.
     */
    @Before
    public void setUp() {
        // Initialize objects for testing
        taskRecorder = new TaskRecorder();
        commandRecorder = new HistoryCommandRecorder();
        criterionRecorder = new CriterionRecorder();
        parameters = new String[]{"param1"};
    }

    /**
     * Tests the getInstructionName method of the InstructionEnumeration class.
     */
    @Test
    public void testGetInstructionName() {
        assertEquals("CreatePrimitiveTask", InstructionEnumeration.CreatePrimitiveTask.getInstructionName());
        assertEquals("CreateCompositeTask", InstructionEnumeration.CreateCompositeTask.getInstructionName());
        // Continue for the rest of the instructions...
    }

    /**
     * Tests the getExecutor method of the InstructionEnumeration class.
     */
    @Test
    public void testGetExecutor() {
        assertTrue(InstructionEnumeration.CreatePrimitiveTask.getExecutor() instanceof CreatePrimitiveTaskExecutor);
        assertTrue(InstructionEnumeration.CreateCompositeTask.getExecutor() instanceof CreateCompositeTaskExecutor);
        // Continue for the rest of the instructions...
    }

    /**
     * Tests the getExecutorStatus method of the InstructionEnumeration class.
     */
    @Test
    public void testGetExecutorStatus() {
        assertFalse(InstructionEnumeration.CreatePrimitiveTask.getExecutorStatus());
        assertFalse(InstructionEnumeration.CreateCompositeTask.getExecutorStatus());
        // Continue for the rest of the instructions...
    }

    /**
     * Tests the executeInstruction method of the InstructionEnumeration class.
     * Note: This is a simple test. In a real-world scenario, you would need to test the effect of the execution.
     */
    @Test
    public void testExecuteInstruction() {
        try {
            InstructionEnumeration.CreatePrimitiveTask.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
            InstructionEnumeration.CreateCompositeTask.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
            // Continue for the rest of the instructions...
        } catch (Exception e) {
            fail("Execution of instruction should not throw an exception.");
        }
    }

    /**
     * Tests the getInstructionMap method of the InstructionEnumeration class.
     */
    @Test
    public void testGetInstructionMap() {
        Map<String, InstructionEnumeration> instructionMap = InstructionEnumeration.getInstructionMap();
        assertEquals(InstructionEnumeration.CreatePrimitiveTask, instructionMap.get("CreatePrimitiveTask"));
        assertEquals(InstructionEnumeration.CreateCompositeTask, instructionMap.get("CreateCompositeTask"));

    }
}
