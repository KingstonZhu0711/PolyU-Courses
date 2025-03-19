package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DefineNegatedCriterionExecutorTest {

    private TaskRecorder taskRecorder;
    private HistoryCommandRecorder commandRecorder;
    private CriterionRecorder criterionRecorder;
    private Criterion criterion;

    @Before
    public void setUp() {
        taskRecorder = new TaskRecorder();
        commandRecorder = new HistoryCommandRecorder();
        criterionRecorder = new CriterionRecorder();
        criterion = new BasicCriterion("existingCriterion", "property", "op", "value");
        criterionRecorder.addCriterion(criterion);
    }

    @Test
    public void testExecuteInstruction() {
        DefineNegatedCriterionExecutor executor = new DefineNegatedCriterionExecutor();

        // Test with valid parameters
        String[] parameters = new String[]{"NewCriterion", "existingCriterion"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertTrue(executor.getStatus());

        // Test with invalid parameters
        parameters = new String[]{"NewCriterion", "nonExistingCriterion"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertTrue(executor.getStatus());
    }

    @Test
    public void testUndoInstruction() {
        DefineNegatedCriterionExecutor executor = new DefineNegatedCriterionExecutor();

        // Execute an instruction
        String[] parameters = new String[]{"NewCriterion", "existingCriterion"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertTrue(executor.getStatus());

        // Undo the instruction
        executor.undoInstruction(new String[]{"NewCriterion"}, taskRecorder, commandRecorder, criterionRecorder);
        assertNull(criterionRecorder.getCriterion("NewCriterion"));
    }
}
