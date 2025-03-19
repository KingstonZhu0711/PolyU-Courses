package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class UndoExecutorTest {

    @Test
    public void testUndoExecutor() {
        // Initialize UndoExecutor
        UndoExecutor undoExecutor = new UndoExecutor();

        // Verify that getStatus() returns false
        assertFalse(undoExecutor.getStatus());

        TaskRecorder taskRecorder = new TaskRecorder();
        HistoryCommandRecorder commandRecorder = new HistoryCommandRecorder();
        CriterionRecorder criterionRecorder = new CriterionRecorder();
        String[] parameters = new String[0];

        undoExecutor.undoInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        undoExecutor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);

        undoExecutor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);

        undoExecutor.undoInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);

    }

}
