package hk.edu.polyu.comp.comp2021.tms.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for Task in the task management system.
 * It includes tests for methods in Task class to ensure they are functioning as expected.
 *
 * @author Liu Yuyang
 * @version 1.0
 */
public class TaskTest {

    /**
     * Test for constructor and getter methods.
     */
    @Test
    public void constructorAndGettersTest() {
        Task[] prerequisites = new Task[0];
        Task[] subTasks = new Task[0];
        Task task = new Task("task1","description1", 1.0, prerequisites, subTasks, true);

        assertEquals("task1", task.getName());
        assertEquals("description1", task.getDescription());
        assertEquals(1.0, task.getDuration(), 0.01);
        assertArrayEquals(prerequisites, task.getDirectPrerequisites());
        assertArrayEquals(subTasks, task.getSubTasks());
        assertTrue(task.getType());
    }

    /**
     * Test for setter methods.
     */
    @Test
    public void settersTest() {
        Task[] prerequisites = new Task[0];
        Task[] subTasks = new Task[0];
        Task task = new Task("task1","description1", 1.0, prerequisites, subTasks, true);

        task.setName("task2");
        task.setDescription("description2");
        task.setDuration(2.0);
        task.setDirectPrerequisites(new Task[0]);

        assertEquals("task2", task.getName());
        assertEquals("description2", task.getDescription());
        assertEquals(2.0, task.getDuration(), 0.01);
    }

    /**
     * Test for isValidName method.
     */
    @Test
    public void isValidNameTest() {
        assertTrue(Task.isValidName("task1"));
        assertFalse(Task.isValidName("1task"));
        assertFalse(Task.isValidName("task@"));
        assertFalse(Task.isValidName("longtaskname"));
        assertFalse(Task.isValidName(null));
    }

    /**
     * Test for isValidDescription method.
     */
    @Test
    public void isValidDescriptionTest() {
        assertTrue(Task.isValidDescription("description-1"));
        assertFalse(Task.isValidDescription("description@"));
    }

    /**
     * Test for isValidDuration and isValidDurationInput method.
     */
    @Test
    public void isValidDurationTest() {
        assertTrue(Task.isValidDuration(1.0));
        assertFalse(Task.isValidDuration(0));

        assertTrue(Task.isValidDurationInput("1.0"));
        assertFalse(Task.isValidDurationInput("notanumber"));
    }

    /**
     * Test for isValidPrerequisiteInput and isValidSubtaskInput method.
     */
    @Test
    public void isValidPrerequisiteInputTest() {
        assertTrue(Task.isValidPrerequisiteInput("task1,task2"));
        assertFalse(Task.isValidPrerequisiteInput("task1 task2"));

        assertTrue(Task.isValidSubtaskInput("task1,task2"));
        assertFalse(Task.isValidSubtaskInput("task1 task2"));
    }

    /**
     * Test for getIndirectPrerequisites method.
     */
    @Test
    public void getIndirectPrerequisitesTest() {
        Task[] prerequisites = new Task[0];
        Task[] subTasks = new Task[0];
        Task task = new Task("task1","description1", 1.0, prerequisites, subTasks, true);

        assertArrayEquals(prerequisites, task.getIndirectPrerequisites());
    }

    /**
     * Test for getTaskList method.
     */
    @Test
    public void getTaskListTest() {
        TaskRecorder taskRecorder = new TaskRecorder();
        Task task1 = new Task("task1","description1", 1.0, new Task[0], new Task[0], true);
        Task task2 = new Task("task2","description2", 1.0, new Task[0], new Task[0], true);
        taskRecorder.addTask(task1);
        taskRecorder.addTask(task2);

        String[] names = new String[]{"task1", "task2"};
        Task[] expectedTaskList = new Task[]{task1, task2};
        assertArrayEquals(expectedTaskList, Task.getTaskList(names, taskRecorder));
    }

    /**
     * Test for contains method.
     */
    @Test
    public void containsTest() {
        Task task1 = new Task("task1","description1", 1.0, new Task[0], new Task[0], true);
        Task task2 = new Task("task2","description2", 1.0, new Task[0], new Task[0], true);
        List<Task> taskList = new ArrayList<>();
        taskList.add(task1);

        assertTrue(Task.contains(taskList, task1));
        assertFalse(Task.contains(taskList, task2));
    }

}
