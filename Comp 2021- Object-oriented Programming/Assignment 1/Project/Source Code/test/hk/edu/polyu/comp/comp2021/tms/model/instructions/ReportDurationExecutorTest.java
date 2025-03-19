package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ReportDurationExecutorTest {
    private ReportDurationExecutor executor;
    private TaskRecorder taskRecorder;
    private HistoryCommandRecorder commandRecorder;
    private CriterionRecorder criterionRecorder;

    @Before
    public void setUp() {
        executor = new ReportDurationExecutor();
        taskRecorder = new TaskRecorder();
        commandRecorder = new HistoryCommandRecorder();
        criterionRecorder = new CriterionRecorder();

        // Assuming Task class has a constructor that takes a name, description, duration, directPrerequisites, subTasks, and type
        Task task = new Task("Test Task", "This is a test task", 10, new Task[0], new Task[0], false);

        // Assuming TaskRecorder has a method to add a task
        taskRecorder.addTask(task);
    }

    @Test
    public void getStatusTest() {
        assertFalse(executor.getStatus());
    }

    @Test
    public void executeInstructionTest() {
        // Correct parameters
        executor.executeInstruction(new String[] {"Test Task"}, taskRecorder, commandRecorder, criterionRecorder);

        // Incorrect parameters
        executor.executeInstruction(new String[] {"Test Task", "Extra Parameter"}, taskRecorder, commandRecorder, criterionRecorder);
    }

    @Test
    public void undoInstructionTest() {
        executor.undoInstruction(new String[] {}, taskRecorder, commandRecorder, criterionRecorder);
    }
}
