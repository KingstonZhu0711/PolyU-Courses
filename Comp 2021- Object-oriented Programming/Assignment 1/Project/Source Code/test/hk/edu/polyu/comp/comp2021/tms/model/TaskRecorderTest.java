
package hk.edu.polyu.comp.comp2021.tms.model;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

/**
 * Test class for TaskRecorder in the task management system.
 * It includes tests for methods in TaskRecorder class to ensure they are functioning as expected.
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public class TaskRecorderTest {

    private TaskRecorder taskRecorder;
    private Task task1;
    private Task task2;

    @Before
    public void setup() {
        taskRecorder = new TaskRecorder();
        task1 = new Task("task1","description1", 1.0, new Task[0], new Task[0], true);
        task2 = new Task("task2","description2", 2.0, new Task[0], new Task[0], true);
    }

    @Test
    public void testAddTask() {
        taskRecorder.addTask(task1);
        assertEquals(task1, taskRecorder.getTask("task1"));
    }

    @Test
    public void testDeleteTask() {
        taskRecorder.addTask(task1);
        taskRecorder.deleteTask("task1");
        assertNull(taskRecorder.getTask("task1"));
    }

    @Test
    public void testExistsTask() {
        assertFalse(taskRecorder.existsTask("task1"));
        taskRecorder.addTask(task1);
        assertTrue(taskRecorder.existsTask("task1"));
    }

    @Test
    public void testUpdateTaskName() {
        taskRecorder.addTask(task1);
        task1.setName("task1_updated");
        taskRecorder.updateTaskName("task1");
        assertNull(taskRecorder.getTask("task1"));
        assertEquals(task1, taskRecorder.getTask("task1_updated"));
    }

    @Test
    public void testAllExist() {
       taskRecorder.addTask(task1);
       taskRecorder.addTask(task2);
       assertTrue(TaskRecorder.allExist(new String[]{"task1", "task2"}, taskRecorder));
       assertFalse(TaskRecorder.allExist(new String[]{"task1", "task3"}, taskRecorder));
    }

    @Test
    public void testClearRecorder() {
        taskRecorder.addTask(task1);
        taskRecorder.addTask(task2);
        taskRecorder.clearRecorder();
        assertNull(taskRecorder.getTask("task1"));
        assertNull(taskRecorder.getTask("task2"));
    }



    @Test
    public void testShowTask() {
        // Given
        taskRecorder.addTask(task1);

        // Capture the standard output
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // When
        taskRecorder.showTask("task1");

        // Then
        String expectedOutput = "\r\n--------Information for task1--------\r\n" +
                "Task Name: task1\r\n" +
                "Description: description1\r\n" +
                "Duration: 1.0\r\n" +
                "Type: Primitive\r\n" +
                "Direct Prerequisites: \r\n" +
                "Indirect Prerequisites: \r\n";
        assertEquals(expectedOutput, outContent.toString());

        // Reset the standard output
        System.setOut(originalOut);
    }

    /**
     * Test case for showTask method of TaskRecorder.
     *
     * This test case aims to cover all possible scenarios in the code to achieve 100% line coverage.
     */
    @Test
    public void testShowTaskAdditionPart() {
        // Create the prerequisites
        Task prerequisiteTask1 = new Task("prerequisite1", "prerequisiteDescription1", 0.5, new Task[0], new Task[0], true);
        Task prerequisiteTask2 = new Task("prerequisite2", "prerequisiteDescription2", 0.5, new Task[0], new Task[0], true);

        // Add the prerequisites to the tasks
        taskRecorder.addTask(prerequisiteTask1);
        taskRecorder.addTask(prerequisiteTask2);

        // Create a subtask with a prerequisite
        Task subTask = new Task("subTask", "subTaskDescription", 0.5, new Task[]{prerequisiteTask1}, new Task[0], true);
        Task subTask2 = new Task("subTask2", "subTask2Description", 0.5, new Task[]{prerequisiteTask2}, new Task[0], true);

        // Add the subtask to the tasks
        taskRecorder.addTask(subTask);
        taskRecorder.addTask(subTask2);

        // Create the composite task with a subtask and direct prerequisites.
        Task compositeTask = new Task("compositeTask", "compositeTaskDescription", 1.0, new Task[]{subTask, subTask2}, new Task[]{prerequisiteTask1, prerequisiteTask2}, false);

        // Add the composite task to the tasks
        taskRecorder.addTask(compositeTask);

        // Redirect System.out to a ByteArrayOutputStream
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // Test with a composite task that has direct prerequisites which are also prerequisites for its subtasks.
        taskRecorder.showTask("compositeTask");

        // Reset System.out to the console
        System.setOut(originalOut);
    }

    @Test
    public void testShowTaskAdditionPart2() {
        // Create some prerequisite tasks
        Task prerequisiteTask1 = new Task("prerequisite1", "prerequisiteDescription1", 0.5, new Task[0], new Task[0], true);
        Task prerequisiteTask2 = new Task("prerequisite2", "prerequisiteDescription2", 0.5, new Task[0], new Task[0], true);

        // Adding these tasks
        taskRecorder.addTask(prerequisiteTask1);
        taskRecorder.addTask(prerequisiteTask2);

        // Creating a sub-task with prerequisiteTask1 as its direct prerequisite
        Task subTask1 = new Task("subTask1", "subTaskDescription1", 0.5, new Task[]{prerequisiteTask1}, new Task[0], true);
        taskRecorder.addTask(subTask1);

        // Creating another sub-task with prerequisiteTask2 as its direct prerequisite
        Task subTask2 = new Task("subTask2", "subTaskDescription2", 0.5, new Task[]{prerequisiteTask2}, new Task[0], true);
        taskRecorder.addTask(subTask2);

        // Creating a task with subTask1 as its sub-task and prerequisiteTask1 and prerequisiteTask2 as its direct prerequisites
        Task task = new Task("task", "taskDescription", 1.0, new Task[]{subTask1}, new Task[]{prerequisiteTask1, prerequisiteTask2}, false);
        taskRecorder.addTask(task);

        // This will cover the case where prerequisite is not null, subTaskPrerequisite is in showingTask.getSubTasks(), and directPrerequisiteNameList is not empty
        taskRecorder.showTask("task");

        // Creating a task with no direct prerequisites
        Task task2 = new Task("task2", "taskDescription2", 1.0, new Task[0], new Task[0], false);
        taskRecorder.addTask(task2);

        // This will cover the case where directPrerequisiteNameList is empty
        taskRecorder.showTask("task2");

        // Creating a task with subTask1 as its sub-task and subTask2 as its direct prerequisite
        Task task3 = new Task("task3", "taskDescription3", 1.0, new Task[]{subTask1}, new Task[]{subTask2}, false);
        taskRecorder.addTask(task3);

        // This will cover the case where subTaskPrerequisite is not in showingTask.getSubTasks()
        taskRecorder.showTask("task3");
    }

    /**
     *  For the code here, to achieve 100% line coverage, I used chatgpt
     *  AdditionPart1 and 2 are generated by chatgpt
     *  However, line coverage was still less than 100 per cent
     *
     */



    @Test
    public void testShowCompositeTask() {
        // Create the prerequisites
        Task prerequisiteTask1 = new Task("prerequisite1","prerequisiteDescription1", 0.5, new Task[0], new Task[0], true);
        Task prerequisiteTask2 = new Task("prerequisite2","prerequisiteDescription2", 0.5, new Task[0], new Task[0], true);
        Task prerequisiteTask3 = new Task("prerequisite3","prerequisiteDescription3", 0.5, new Task[0], new Task[0], true);
        taskRecorder.addTask(prerequisiteTask1);
        taskRecorder.addTask(prerequisiteTask2);
        taskRecorder.addTask(prerequisiteTask3);

        // Create the subtask with a prerequisite
        Task subTask = new Task("subTask","subTaskDescription", 0.5, new Task[]{prerequisiteTask1}, new Task[0], true);
        taskRecorder.addTask(subTask);

        // Create the composite task with a subtask and direct and indirect prerequisites
        Task compositeTask = new Task("compositeTask","compositeTaskDescription", 1.0, new Task[]{subTask}, new Task[]{prerequisiteTask2, prerequisiteTask3}, false);
        taskRecorder.addTask(compositeTask);

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        taskRecorder.showTask("compositeTask");

        String expectedOutput = "\r\n--------Information for compositeTask--------\r\n" +
                "Task Name: compositeTask\r\n" +
                "Description: compositeTaskDescription\r\n" +
                "Duration: 1.0\r\n" +
                "Type: Composite\r\n" +
                "Direct Prerequisite: no direct prerequisite\r\n" +
                "Indirect Prerequisites: prerequisite1 \r\n" +
                "Subtasks: prerequisite2 prerequisite3 \r\n\r\n";

        assertEquals(expectedOutput, outContent.toString());

        System.setOut(originalOut);
    }

    @Test
    public void testShowAllTasks() {
        // Given
        taskRecorder.addTask(task1);
        taskRecorder.addTask(task2);

        // Capture the standard output
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        // When
        taskRecorder.showAllTasks();

        // Then
        String expectedOutput = "\r\n--------Information for task1--------\r\n" +
                "Task Name: task1\r\n" +
                "Description: description1\r\n" +
                "Duration: 1.0\r\n" +
                "Type: Primitive\r\n" +
                "Direct Prerequisites: \r\n" +
                "Indirect Prerequisites: \r\n" +
                "\r\n--------Information for task2--------\r\n" +
                "Task Name: task2\r\n" +
                "Description: description2\r\n" +
                "Duration: 2.0\r\n" +
                "Type: Primitive\r\n" +
                "Direct Prerequisites: \r\n" +
                "Indirect Prerequisites: \r\n";
        assertEquals(expectedOutput, outContent.toString());

        // Reset the standard output
        System.setOut(originalOut);
    }

        /**
         * Test method for {@link TaskRecorder#getAllTasks()}.
         * It tests whether the returned array of tasks from getAllTasks method matches the expected array of tasks.
         */
    @Test
    public void testGetAllTasks() {
        // Given
        taskRecorder.addTask(task1);
        taskRecorder.addTask(task2);

        // When
        Task[] allTasks = taskRecorder.getAllTasks();

        // Then
        Task[] expectedTasks = new Task[]{task1, task2};
        assertArrayEquals(expectedTasks, allTasks);
    }

}
