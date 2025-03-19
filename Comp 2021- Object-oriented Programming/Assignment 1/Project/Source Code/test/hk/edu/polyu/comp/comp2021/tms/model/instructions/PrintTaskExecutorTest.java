package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class PrintTaskExecutorTest {

    @Test
    public void testPrintTaskExecutor() {
        PrintTaskExecutor executor = new PrintTaskExecutor();

        TaskRecorder taskRecorder = new TaskRecorder();
        HistoryCommandRecorder commandRecorder = new HistoryCommandRecorder();
        CriterionRecorder criterionRecorder = new CriterionRecorder();

        Task testTask = new Task("TestTask", "TestTask", 1.0, new Task[0], new Task[0], true);
        taskRecorder.addTask(testTask);

        executor.executeInstruction(new String[]{"TestTask"}, taskRecorder, commandRecorder, criterionRecorder);
        executor.executeInstruction(new String[0], taskRecorder, commandRecorder, criterionRecorder);
        executor.executeInstruction(new String[]{"NonExistentTask"}, taskRecorder, commandRecorder, criterionRecorder);
        executor.undoInstruction(new String[0], taskRecorder, commandRecorder, criterionRecorder);
    }

    private ChangeTaskExecutor executor;
    private TaskRecorder taskRecorder;
    private HistoryCommandRecorder commandRecorder;
    private CriterionRecorder criterionRecorder;

    @Before
    public void setUp() {
        executor = new ChangeTaskExecutor();
        taskRecorder = new TaskRecorder();
        commandRecorder = new HistoryCommandRecorder();
        criterionRecorder = new CriterionRecorder();
        Task task = new PrimitiveTask("Task1", "Description", 5, new Task[0]);
        taskRecorder.addTask(task);
    }




    @Test
    public void testGetStatusFalse() {
        String[] parameters = {"NonExistentTask", "name", "NewTaskName"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
    }

    @Test
    public void testGetStatus() {
        SearchExecutor executor = new SearchExecutor();
        assertFalse(executor.getStatus());
    }
}
