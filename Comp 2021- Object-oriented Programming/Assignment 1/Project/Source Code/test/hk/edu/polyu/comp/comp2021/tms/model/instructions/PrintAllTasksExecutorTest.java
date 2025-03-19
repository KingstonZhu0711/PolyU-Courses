package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;
import org.junit.Before;
import org.junit.Test;

public class PrintAllTasksExecutorTest {

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
    public void testExecuteInstructionWithNoParameters() {
        PrintAllTasksExecutor executor = new PrintAllTasksExecutor();

        String[] parameters = new String[]{};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
    }

    @Test
    public void testExecuteInstructionWithParameters() {
        PrintAllTasksExecutor executor = new PrintAllTasksExecutor();

        String[] parameters = new String[]{"parameter1", "parameter2"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
    }

    @Test
    public void testUndoInstruction() {
        PrintAllTasksExecutor executor = new PrintAllTasksExecutor();

        String[] parameters = new String[]{};
        executor.undoInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
    }

    @Test
    public void testGetStatus() {
        PrintAllTasksExecutor executor = new PrintAllTasksExecutor();
        executor.getStatus();
    }
}
