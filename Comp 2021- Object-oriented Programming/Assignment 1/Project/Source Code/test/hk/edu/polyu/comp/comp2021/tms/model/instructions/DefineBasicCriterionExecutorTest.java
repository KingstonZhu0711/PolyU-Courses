package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefineBasicCriterionExecutorTest {

    private TaskRecorder taskRecorder;
    private HistoryCommandRecorder commandRecorder;
    private CriterionRecorder criterionRecorder;

    @Before
    public void setUp() {
        taskRecorder = new TaskRecorder(); // Assuming these classes have default constructors
        commandRecorder = new HistoryCommandRecorder();
        criterionRecorder = new CriterionRecorder();
    }

    @Test
    public void testExecuteInstructionWithInvalidParameters() {
        DefineBasicCriterionExecutor executor = new DefineBasicCriterionExecutor();

        // Test with invalid parameters
        String[] parameters1 = new String[]{"name", "property", "op"};
        executor.executeInstruction(parameters1, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
    }

    @Test
    public void testExecuteInstructionWithIsPrimitiveName() {
        DefineBasicCriterionExecutor executor = new DefineBasicCriterionExecutor();

        // Test with IsPrimitive as the name
        String[] parameters2 = new String[]{"IsPrimitive", "property", "op", "value"};
        executor.executeInstruction(parameters2, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
    }

    @Test
    public void testExecuteInstructionWithValidParameters() {
        DefineBasicCriterionExecutor executor = new DefineBasicCriterionExecutor();

        // Test with valid parameters
        String[] parameters3 = new String[]{"name1", "property1", "op1", "value1"};
        executor.executeInstruction(parameters3, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
    }

    @Test
    public void testExecuteInstructionWithRepeatedName() {
        DefineBasicCriterionExecutor executor = new DefineBasicCriterionExecutor();

        // Test with valid parameters
        String[] parameters3 = new String[]{"name2", "property2", "op2", "value2"};
        executor.executeInstruction(parameters3, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());

        // Test with repeated name
        executor = new DefineBasicCriterionExecutor();
        executor.executeInstruction(parameters3, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
    }

    @Test
    public void testUndoInstruction() {
        DefineBasicCriterionExecutor executor = new DefineBasicCriterionExecutor();
        String[] parameters = new String[]{"name3", "property3", "op3", "value3"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());

        executor.undoInstruction(new String[]{"name"}, taskRecorder, commandRecorder, criterionRecorder);
        // Validate that the criterion with the name "name" was removed from the criterionRecorder
    }
}
