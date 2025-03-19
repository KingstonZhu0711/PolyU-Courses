package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ReportEarliestFinishTimeExecutorTest {

    private ReportEarliestFinishTimeExecutor executor;
    private TaskRecorder taskRecorder;
    private HistoryCommandRecorder commandRecorder;
    private CriterionRecorder criterionRecorder;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        executor = new ReportEarliestFinishTimeExecutor();
        taskRecorder = new TaskRecorder();
        commandRecorder = new HistoryCommandRecorder();
        criterionRecorder = new CriterionRecorder();

        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testGetStatus() {
        assertFalse(executor.getStatus());
    }

    @Test
    public void testExecuteInstructionWithInvalidParameters() {
        executor.executeInstruction(new String[]{}, taskRecorder, commandRecorder, criterionRecorder);
        assertTrue(outContent.toString().contains("Invalid input parameters"));
    }

    @Test
    public void testExecuteInstructionWithNonExistentTask() {
        executor.executeInstruction(new String[]{"NonExistentTask"}, taskRecorder, commandRecorder, criterionRecorder);
        assertTrue(outContent.toString().contains("The input task does not exist"));
    }

    @Test
    public void testExecuteInstructionWithPrerequisites() {
        // Create tasks
        Task task2 = new Task("Task2", "Description2", 3.0, new Task[]{}, new Task[]{}, false);
        Task task3 = new Task("Task3", "Description3", 2.0, new Task[]{}, new Task[]{}, false);
        Task task1 = new Task("Task1", "Description1", 5.0, new Task[]{}, new Task[]{}, false);

        // Set direct prerequisites
        task1.setDirectPrerequisites(new Task[]{task2, task3});

        // Assuming TaskRecorder has a method addTask to add tasks
        taskRecorder.addTask(task1);
        taskRecorder.addTask(task2);
        taskRecorder.addTask(task3);

        executor.executeInstruction(new String[]{"Task1"}, taskRecorder, commandRecorder, criterionRecorder);

        assertTrue(outContent.toString().contains("--------Earliest Finish Time of Task1--------"));
        assertTrue(outContent.toString().contains("The earliest finish time: 10.0"));
    }

    @Test
    public void testUndoInstruction() {
        executor.undoInstruction(new String[]{}, taskRecorder, commandRecorder, criterionRecorder);
        // No output or state change expected for this operation.
    }
}
