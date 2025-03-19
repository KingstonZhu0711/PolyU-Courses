package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;
import hk.edu.polyu.comp.comp2021.tms.model.instructions.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CreatePrimitiveTaskExecutorTest {

    @Before
    public void setup() {
        createPrimitiveTaskExecutor = new CreatePrimitiveTaskExecutor();
        taskRecorder = new TaskRecorder();
        commandRecorder = new HistoryCommandRecorder();
        criterionRecorder = new CriterionRecorder();
        // Add more setup if necessary.
    }

    @Test
    public void testCreatePrimitiveTaskExecutor() {
        TaskRecorder taskRecorder = new TaskRecorder();
        HistoryCommandRecorder commandRecorder = new HistoryCommandRecorder();
        CriterionRecorder criterionRecorder = new CriterionRecorder();

        CreatePrimitiveTaskExecutor executor = new CreatePrimitiveTaskExecutor();
        assertFalse(executor.getStatus());

        String[] parameters = {""};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());

        parameters = new String[]{"task1", "description", "1.0", "task2"};
        taskRecorder.addTask(new PrimitiveTask("task2", "description", 1.0, new Task[0]));
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertTrue(executor.getStatus() || taskRecorder.getTask("task1") != null);

        parameters = new String[]{"task1", "description", "1.0", "task1"};
        taskRecorder.addTask(new PrimitiveTask("task1", "description", 1.0, new Task[0]));
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
        taskRecorder.deleteTask("task1");

        // And so on for each validation

        parameters = new String[]{"task1,description,1.0,task2"};
        taskRecorder.addTask(new PrimitiveTask("task2", "description", 1.0, new Task[0]));
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertTrue(executor.getStatus());

        executor.undoInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertNull(taskRecorder.getTask("task1"));
    }


    private CreatePrimitiveTaskExecutor createPrimitiveTaskExecutor;
    private TaskRecorder taskRecorder;
    private HistoryCommandRecorder commandRecorder;
    private CriterionRecorder criterionRecorder;


    @Test
    public void testGetStatus() {
        assertFalse(createPrimitiveTaskExecutor.getStatus());
    }

    @Test
    public void testExecuteInstruction() {
        // Add prerequisite tasks to taskRecorder before running this test.
        createPrimitiveTaskExecutor.executeInstruction(new String[]{"TaskName", "TaskDescription", "TaskDuration", "PreReqTask1,PreReqTask2"}, taskRecorder, commandRecorder, criterionRecorder);
        // Capture the System.out content and check if it contains "The primitive is successfully created".
        // Also check if the task has been added to the taskRecorder.
    }

    @Test
    public void testExecuteInstructionNonExistentPrerequisites() {
        createPrimitiveTaskExecutor.executeInstruction(new String[]{"TaskName", "TaskDescription", "TaskDuration", "NonExistentPreReq"}, taskRecorder, commandRecorder, criterionRecorder);
        // Capture the System.out content and check if it contains "The input prerequisite(s) has not been created".
    }

    @Test
    public void testUndoInstruction() {
        // Add a task to taskRecorder before running this test.
        createPrimitiveTaskExecutor.undoInstruction(new String[]{"ExistingTaskName"}, taskRecorder, commandRecorder, criterionRecorder);
        // Check if the task has been removed from the taskRecorder.
    }

    @Test
    public void testExecuteInstructionWithNonexistentPrerequisites() {


        createPrimitiveTaskExecutor.executeInstruction(new String[]{"TaskName", "TaskDescription", "1", "NonExistentPreReq1,NonExistentPreReq2"}, taskRecorder, commandRecorder, criterionRecorder);

    }

    @Test
    public void testExecuteInstructionWithExistingPrerequisites() {

        Task preReqTask1 = new PrimitiveTask("PreReqTask1", "Description", 1, null);
        Task preReqTask2 = new PrimitiveTask("PreReqTask2", "Description", 1, null);

        if (preReqTask1 != null && preReqTask2 != null) {
            taskRecorder.addTask(preReqTask1);
            taskRecorder.addTask(preReqTask2);
        }

        String prerequisitesInput = "PreReqTask1,PreReqTask2";
        createPrimitiveTaskExecutor.executeInstruction(new String[]{"TaskName", "TaskDescription", "1", prerequisitesInput}, taskRecorder, commandRecorder, criterionRecorder);

    }
}
