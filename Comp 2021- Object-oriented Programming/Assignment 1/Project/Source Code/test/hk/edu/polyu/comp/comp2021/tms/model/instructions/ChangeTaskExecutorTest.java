package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class ChangeTaskExecutorTest {

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
    public void testExecuteInstruction() {
        // Create a task
        Task task = new PrimitiveTask("task1", "task1", 1.0, new Task[0]);
        taskRecorder.addTask(task);

        // Test changing name
        String[] parameters = {"task1", "name", "newTask1"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertEquals(executor.getChangedProperty(), "name");

        // Test changing description
        parameters = new String[]{"newTask1", "description", "newDescription"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertEquals(executor.getChangedProperty(), "description");

        // Test changing prerequisites
        Task task2 = new PrimitiveTask("task2", "task2", 1.0, new Task[0]);
        taskRecorder.addTask(task2);
        parameters = new String[]{"newTask1", "prerequisites", "task2"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertEquals(executor.getChangedProperty(), "description");

        // Test changing subtasks
        Task task3 = new CompositeTask("task3", "task3", new Task[]{task});
        taskRecorder.addTask(task3);
        parameters = new String[]{"task3", "subtasks", "newTask1"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertEquals(executor.getChangedProperty(), "description");


    }

    @Test
    public void testUndoInstruction() {
        // Create a task
        Task task = new PrimitiveTask("task1", "task1", 1.0, new Task[0]);
        taskRecorder.addTask(task);

        // Test undo changing name
        String[] parameters = {"task1", "name", "newTask1"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        executor.undoInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertEquals(task.getName(), "task1");
    }

    @Test
    public void testDescriptionUpdate() {
        Task task = new PrimitiveTask("task1", "description1", 1.0, new Task[0]);
        taskRecorder.addTask(task);
        String[] parameters = {"task1", "description", "newDescription"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertEquals("newDescription", task.getDescription());
    }

    @Test
    public void testInvalidDescriptionUpdate() {
        Task task = new PrimitiveTask("task1", "description1", 1.0, new Task[0]);
        taskRecorder.addTask(task);
        String[] parameters = {"task1", "description", ""}; // empty string is invalid
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertEquals("", task.getDescription()); // Description should not change
    }

    @Test
    public void testDurationUpdate() {
        Task task = new PrimitiveTask("task1", "description1", 1.0, new Task[0]);
        taskRecorder.addTask(task);
        String[] parameters = {"task1", "duration", "2.0"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertEquals(2.0, task.getDuration(), 0.001);
    }

    @Test
    public void testInvalidDurationUpdate() {
        Task task = new PrimitiveTask("task1", "description1", 1.0, new Task[0]);
        taskRecorder.addTask(task);
        String[] parameters = {"task1", "duration", "-1.0"}; // Negative duration is invalid
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertEquals(1.0, task.getDuration(), 0.001); // Duration should not change
    }

    @Test
    public void testChangeDuration() {
        String[] parameters = {"Task1", "duration", "10"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertTrue(executor.getStatus());
        assertEquals("duration", executor.getChangedProperty());
        assertEquals("5.0", executor.getChangedContent());
    }



    @Test
    public void testChangePrerequisites() {
        // Add another task to be a prerequisite
        Task task2 = new PrimitiveTask("Task2", "Description", 5, new Task[0]);
        taskRecorder.addTask(task2);
        String[] parameters = {"Task1", "prerequisites", "Task2"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
        assertEquals(null , executor.getChangedProperty());
        // assertEquals(); This would be the previous array of prerequisites
    }

    @Test
    public void testInvalidProperty() {
        String[] parameters = {"Task1", "InvalidProperty", "SomeValue"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
    }

    @Test
    public void testInvalidTaskName() {
        String[] parameters = {"NonExistentTask", "name", "NewName"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
    }

    @Test
    public void testInvalidParameterLength() {
        String[] parameters = {"Task1", "name"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
    }

    @Test
    public void testEmptyString() {
        String[] parameters = {"", "name", "NewName"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());

        parameters = new String[]{"Task1", "", "NewName"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());

    }


    @Test
    public void testValidPrerequisiteUpdate() {
        // Create three tasks
        Task task1 = new PrimitiveTask("task1", "task1", 1.0, new Task[0]);
        Task task2 = new PrimitiveTask("task2", "task2", 1.0, new Task[0]);
        Task task3 = new PrimitiveTask("task3", "task3", 1.0, new Task[0]);
        taskRecorder.addTask(task1);
        taskRecorder.addTask(task2);
        taskRecorder.addTask(task3);

        // Execute instruction to update prerequisites of task1 to task2 and task3
        String[] parameters = {"task1", "prerequisites", "task2,task3"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);

        // Check that the prerequisites of task1 have been updated correctly
        Task[] expectedPrerequisites = {task2, task3};
        assertArrayEquals(expectedPrerequisites, task1.getDirectPrerequisites());
    }

    @Test
    public void testInvalidPrerequisiteInput() {
        // Create a task
        Task task = new PrimitiveTask("task1", "task1", 1.0, new Task[0]);
        taskRecorder.addTask(task);

        // Execute instruction with invalid prerequisite input (should print "Invalid prerequisite input")
        String[] parameters = {"task1", "prerequisites", "task2,,task3"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
    }

    @Test
    public void testNonexistentPrerequisite() {
        // Create a task
        Task task = new PrimitiveTask("task1", "task1", 1.0, new Task[0]);
        taskRecorder.addTask(task);

        // Execute instruction with nonexistent prerequisite (should print "Invalid prerequisites. Please provide a valid prerequisites")
        String[] parameters = {"task1", "prerequisites", "task2"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
    }

    @Test
    public void testValidSubtaskUpdate() {
        // Create three tasks
        Task task1 = new CompositeTask("task1", "task1", new Task[0]);
        Task task2 = new PrimitiveTask("task2", "task2", 1.0, new Task[0]);
        Task task3 = new PrimitiveTask("task3", "task3", 1.0, new Task[0]);
        taskRecorder.addTask(task1);
        taskRecorder.addTask(task2);
        taskRecorder.addTask(task3);

        // Execute instruction to update subtasks of task1 to task2 and task3
        String[] parameters = {"task1", "subtasks", "task2,task3"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);

        // Check that the subtasks of task1 have been updated correctly
        Task[] expectedSubtasks = {task2, task3};
        assertArrayEquals(expectedSubtasks, task1.getSubTasks());
    }

    @Test
    public void testInvalidSubtaskInput() {
        // Create a task
        Task task = new CompositeTask("task1", "task1", new Task[0]);
        taskRecorder.addTask(task);

        // Execute instruction with invalid subtask input (should print "Invalid subtask input")
        String[] parameters = {"task1", "subtasks", "task2,,task3"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
    }

    @Test
    public void testNonexistentSubtask() {
        // Create a task
        Task task = new CompositeTask("task1", "task1", new Task[0]);
        taskRecorder.addTask(task);

        // Execute instruction with nonexistent subtask (should print "Invalid subtasks. Please provide a valid subtasks")
        String[] parameters = {"task1", "subtasks", "task2"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
    }

    @Test
    public void testInvalidProperty2() {
        // Create a task
        Task task = new PrimitiveTask("task1", "task1", 1.0, new Task[0]);
        taskRecorder.addTask(task);

        // Execute instruction with invalid property (should print "Invalid property. Please provide a valid property (name, description, or subtasks)")
        String[] parameters = {"task1", "invalidProperty", "newValue"};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
    }

}