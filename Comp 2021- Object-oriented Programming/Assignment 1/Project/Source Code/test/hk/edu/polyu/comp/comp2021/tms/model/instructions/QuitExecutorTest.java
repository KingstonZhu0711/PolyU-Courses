package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.CriterionRecorder;
import hk.edu.polyu.comp.comp2021.tms.model.HistoryCommandRecorder;
import hk.edu.polyu.comp.comp2021.tms.model.TaskRecorder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QuitExecutorTest {
    private static QuitExecutor executor;
    private static TaskRecorder taskRecorder;
    private static HistoryCommandRecorder commandRecorder;
    private static CriterionRecorder criterionRecorder;

    @Before
    public void setUp() {
        executor = new QuitExecutor();
        taskRecorder = new TaskRecorder();
        commandRecorder = new HistoryCommandRecorder();
        criterionRecorder = new CriterionRecorder();
    }


    @Test
    public void testUndoInstruction() {
        executor.undoInstruction(new String[]{}, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
    }

    @Test
    public void testGetStatus() {
        assertFalse(executor.getStatus());

    }

}