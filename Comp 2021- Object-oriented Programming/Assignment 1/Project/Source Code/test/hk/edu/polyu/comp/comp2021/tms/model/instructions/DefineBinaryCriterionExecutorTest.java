package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DefineBinaryCriterionExecutorTest {

    private TaskRecorder taskRecorder;
    private HistoryCommandRecorder commandRecorder;
    private CriterionRecorder criterionRecorder;

    @Before
    public void setUp() {
        taskRecorder = new TaskRecorder();
        commandRecorder = new HistoryCommandRecorder();
        criterionRecorder = new CriterionRecorder();
    }

    @Test
    public void testExecuteInstructionWithInvalidParameters() {
        DefineBinaryCriterionExecutor executor = new DefineBinaryCriterionExecutor();

        // Test with invalid parameters
        String[] parameters1 = new String[]{"name1", "name2", "op"};
        executor.executeInstruction(parameters1, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
    }

    @Test
    public void testExecuteInstructionWithIsPrimitiveName() {
        DefineBinaryCriterionExecutor executor = new DefineBinaryCriterionExecutor();

        // Test with IsPrimitive as the name
        String[] parameters2 = new String[]{"IsPrimitive", "name2", "&&", "name3"};
        executor.executeInstruction(parameters2, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
    }

    @Test
    public void testExecuteInstructionWithRepeatedNames() {
        DefineBinaryCriterionExecutor executor = new DefineBinaryCriterionExecutor();

        // Test with repeated names
        String[] parameters3 = new String[]{"name1", "name1", "&&", "name3"};
        executor.executeInstruction(parameters3, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
    }

    @Test
    public void testExecuteInstructionWithNonExistingCriteria() {
        DefineBinaryCriterionExecutor executor = new DefineBinaryCriterionExecutor();

        // Test with non-existing criteria
        String[] parameters4 = new String[]{"name1", "name2", "&&", "name3"};
        executor.executeInstruction(parameters4, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
    }

    @Test
    public void testExecuteInstructionWithInvalidLogicOperator() {
        DefineBinaryCriterionExecutor executor = new DefineBinaryCriterionExecutor();

        // Add criteria to the recorder
        Criterion criterion1 = new BasicCriterion("name2", "property", "op", "value");
        Criterion criterion2 = new BasicCriterion("name3", "property", "op", "value");
        criterionRecorder.addCriterion(criterion1);
        criterionRecorder.addCriterion(criterion2);

        // Test with invalid logic operator
        String[] parameters5 = new String[]{"name1", "name2", "invalid_op", "name3"};
        executor.executeInstruction(parameters5, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
    }

    @Test
    public void testExecuteInstructionWithValidParameters() {
        DefineBinaryCriterionExecutor executor = new DefineBinaryCriterionExecutor();

        // Test with valid parameters
        String[] parameters6 = new String[]{"name1", "name2", "&&", "name3"};
        executor.executeInstruction(parameters6, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
    }

    @Test
    public void testUndoInstruction() {
        DefineBinaryCriterionExecutor executor = new DefineBinaryCriterionExecutor();

        // Execute an instruction
        String[] parameters = new String[]{"name1", "name2", "&&", "name3"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());

        // Undo the instruction
        executor.undoInstruction(new String[]{"name1"}, taskRecorder, commandRecorder, criterionRecorder);
        assertNull(criterionRecorder.getCriterion("name1"));
    }
}
