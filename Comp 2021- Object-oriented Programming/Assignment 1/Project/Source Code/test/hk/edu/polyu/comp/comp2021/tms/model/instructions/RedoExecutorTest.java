package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.CriterionRecorder;
import hk.edu.polyu.comp.comp2021.tms.model.HistoryCommandRecorder;
import hk.edu.polyu.comp.comp2021.tms.model.TaskRecorder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RedoExecutorTest {
    private RedoExecutor redoExecutor;
    private TaskRecorder taskRecorder;
    private HistoryCommandRecorder commandRecorder;
    private CriterionRecorder criterionRecorder;

    @Before
    public void setUp() {
        redoExecutor = new RedoExecutor();
        taskRecorder = new TaskRecorder();
        commandRecorder = new HistoryCommandRecorder();
        criterionRecorder = new CriterionRecorder();
    }


    @Test
    public void testUndoInstruction() {
        // Check if the Undo operation on RedoExecutor does nothing
        redoExecutor.undoInstruction(new String[]{}, taskRecorder, commandRecorder, criterionRecorder);

        // There should be no changes to the commandRecorder
        assertFalse(commandRecorder.canRedo());
        assertFalse(commandRecorder.canUndo());
    }

    @Test
    public void testGetStatus() {
        // Check if the initial status is false
        assertFalse(redoExecutor.getStatus());
    }

    @Test
    public void testExecuteInstruction() {

        redoExecutor.executeInstruction(new String[]{}, taskRecorder, commandRecorder, criterionRecorder);

        HistoryCommandRecorder.CommandRecord executedCommand = new HistoryCommandRecorder.CommandRecord(redoExecutor, new String[]{});
        commandRecorder.pushExecuted(executedCommand);
        commandRecorder.getUndoingCommand();


        redoExecutor.executeInstruction(new String[]{}, taskRecorder, commandRecorder, criterionRecorder);
    }

}
