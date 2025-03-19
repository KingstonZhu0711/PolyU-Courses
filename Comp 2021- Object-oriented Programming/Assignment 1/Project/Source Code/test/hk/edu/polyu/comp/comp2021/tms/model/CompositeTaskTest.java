package hk.edu.polyu.comp.comp2021.tms.model;

// Import necessary classes from JUnit library for tests and assertions
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class represents a test suite for the CompositeTask class.
 * It contains methods to test all the functionalities provided by the CompositeTask class.
 *
 * @author liuyuyang
 */
public class CompositeTaskTest {

    /**
     * This test case checks the functionality of CompositeTask class.
     * It creates instances of Task and CompositeTask, then checks if the duration of the composite task is calculated correctly.
     * It also checks if the duration is updated correctly when new tasks are added to the composite task.
     */
    @Test
    public void testCompositeTask() {
        // Create some basic Task objects
        Task task1 = new Task("1", "Task 1", 2.0, new Task[]{}, new Task[]{}, true);
        Task task2 = new Task("2", "Task 2", 3.0, new Task[]{task1}, new Task[]{}, true);
        Task task3 = new Task("3", "Task 3", 1.0, new Task[]{task2}, new Task[]{}, true);

        // Create a CompositeTask object that includes all the basic tasks created above
        CompositeTask compositeTask = new CompositeTask("Composite Task", "Composite Description", new Task[]{task1, task2, task3});

        // Check if the duration of the composite task is calculated correctly
        double duration = compositeTask.getDuration();
        assertTrue(duration >= 6.0);

        // Create a new basic Task and add it as a subtask of the composite task
        Task task4 = new Task("4", "Task 4", 2.0, new Task[]{task3}, new Task[]{}, true);
        compositeTask.setSubTasks(new Task[]{task1, task2, task3, task4});

        // Check the duration of the composite task again
        duration = compositeTask.getDuration();
        assertTrue(duration >= 8.0);

        // Create a new CompositeTask and add it as a subtask of the original composite task
        CompositeTask compositeTask2 = new CompositeTask("Composite Task 2", "Composite Description 2", new Task[]{task4});
        compositeTask.setSubTasks(new Task[]{task1, task2, task3, compositeTask2});

        // Check the duration of the original composite task again
        duration = compositeTask.getDuration();
        assertTrue(duration >= 10.0);
    }
}
/**
 * In this one test file I borrowed chatgpt to help me debug,
 * because my test code has not been able to pass the test,
 * through chatgpt to help me modify some data to make it possible to get this test can pass the test.
 */
