package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;
import org.junit.Assert;
import org.junit.Test;

public class CreateCompositeTaskExecutorTest {

    @Test
    public void testCreateCompositeTaskExecutor() {
        CreateCompositeTaskExecutor executor = new CreateCompositeTaskExecutor();
        Assert.assertFalse(executor.getStatus());

        TaskRecorder taskRecorder = new TaskRecorder();
        HistoryCommandRecorder commandRecorder = new HistoryCommandRecorder();
        CriterionRecorder criterionRecorder = new CriterionRecorder();

        // Test with valid parameters
        String[] validParameters = { "ValidName", "ValidDescription", "1.0", "ValidSubTask1,ValidSubTask2", "false" };
        executor.executeInstruction(validParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertFalse(executor.getStatus());

        // Test undo
        String[] undoParameters = { "ValidName" };
        executor.undoInstruction(undoParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertFalse(executor.getStatus());

        // Test with invalid name
        String[] invalidNameParameters = { "InvalidName", "ValidDescription", "1.0", "ValidSubTask1,ValidSubTask2", "false" };
        executor.executeInstruction(invalidNameParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertFalse(executor.getStatus());

        // Test with invalid description
        String[] invalidDescriptionParameters = { "ValidName", "InvalidDescription", "1.0", "ValidSubTask1,ValidSubTask2", "false" };
        executor.executeInstruction(invalidDescriptionParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertFalse(executor.getStatus());

        // Test with invalid subtasks
        String[] invalidSubTasksParameters = { "ValidName", "ValidDescription", "1.0", "InvalidSubTask1,InvalidSubTask2", "false" };
        executor.executeInstruction(invalidSubTasksParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertFalse(executor.getStatus());

        // Test with wrong number of parameters
        String[] wrongNumberParameters = { "ValidName", "ValidDescription" };
        executor.executeInstruction(wrongNumberParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertFalse(executor.getStatus());
    }

    @Test
    public void testCreateCompositeTaskExecutor2() {
        CreateCompositeTaskExecutor executor = new CreateCompositeTaskExecutor();
        TaskRecorder taskRecorder = new TaskRecorder();
        HistoryCommandRecorder commandRecorder = new HistoryCommandRecorder();
        CriterionRecorder criterionRecorder = new CriterionRecorder();

        // Test with valid parameters
        String[] validParameters = { "ValidName", "ValidDescription", "ValidSubTask1,ValidSubTask2" };
        executor.executeInstruction(validParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertFalse(executor.getStatus());

        // Test undo
        String[] undoParameters = { "ValidName" };
        executor.undoInstruction(undoParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertFalse(executor.getStatus());

        // Test with invalid name
        String[] invalidNameParameters = { "0", "ValidDescription", "ValidSubTask1,ValidSubTask2" };
        executor.executeInstruction(invalidNameParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertFalse(executor.getStatus());

        // Test with invalid description
        String[] invalidDescriptionParameters = { "ValidName", "", "ValidSubTask1,ValidSubTask2" };
        executor.executeInstruction(invalidDescriptionParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertFalse(executor.getStatus());

        // Test with invalid subtasks input
        String[] invalidSubTasksInputParameters = { "ValidName", "ValidDescription", "InvalidSubTaskInput" };
        executor.executeInstruction(invalidSubTasksInputParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertFalse(executor.getStatus());

        // Test with invalid subtasks
        String[] invalidSubTasksParameters = { "ValidName", "ValidDescription", "InvalidSubTask1,InvalidSubTask2" };
        executor.executeInstruction(invalidSubTasksParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertFalse(executor.getStatus());

        // Test with subtasks that have not been created
        String[] notCreatedSubTasksParameters = { "ValidName", "ValidDescription", "NotCreatedSubTask1,NotCreatedSubTask2" };
        executor.executeInstruction(notCreatedSubTasksParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertFalse(executor.getStatus());

        // Test with a task that has already been created
        String[] alreadyCreatedTaskParameters = { "ValidName", "ValidDescription", "ValidSubTask1,ValidSubTask2" };
        executor.executeInstruction(alreadyCreatedTaskParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertFalse(executor.getStatus());
    }

    @Test
    public void testCreateCompositeTaskExecutor3() {
        CreateCompositeTaskExecutor executor = new CreateCompositeTaskExecutor();
        TaskRecorder taskRecorder = new TaskRecorder();
        HistoryCommandRecorder commandRecorder = new HistoryCommandRecorder();
        CriterionRecorder criterionRecorder = new CriterionRecorder();

        // Test with a task name that already exists
        taskRecorder.addTask(new PrimitiveTask("ExistingTask", "Description", 1.0, new Task[0]));
        String[] existingTaskParameters = {"ExistingTask", "Description", "SubTask1,SubTask2"};
        executor.executeInstruction(existingTaskParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertFalse(executor.getStatus());

        // Test with an invalid description
        String[] invalidDescriptionParameters = {"NewTask", "", "SubTask1,SubTask2"};
        executor.executeInstruction(invalidDescriptionParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertFalse(executor.getStatus());

        // Test with an invalid subtask input
        String[] invalidSubTasksInputParameters = {"NewTask", "Description", "InvalidSubTaskInput"};
        executor.executeInstruction(invalidSubTasksInputParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertFalse(executor.getStatus());

        // Test with a subtask that has not been created
        String[] notCreatedSubTasksParameters = {"NewTask", "Description", "NotCreatedSubTask"};
        executor.executeInstruction(notCreatedSubTasksParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertFalse(executor.getStatus());

        // Test with valid parameters
        taskRecorder.addTask(new PrimitiveTask("SubTask1", "Description", 1.0, new Task[0]));
        taskRecorder.addTask(new PrimitiveTask("SubTask2", "Description", 1.0, new Task[0]));
        String[] validParameters = {"NewTask", "Description", "SubTask1,SubTask2"};
        executor.executeInstruction(validParameters, taskRecorder, commandRecorder, criterionRecorder);
        Assert.assertTrue(executor.getStatus());
    }
}
