package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class StoreExecutorTest {

    private StoreExecutor storeExecutor;
    private TaskRecorder taskRecorder;
    private HistoryCommandRecorder commandRecorder;
    private CriterionRecorder criterionRecorder;
    private Task task;
    private Criterion criterion;

    @Before
    public void setUp() {
        storeExecutor = new StoreExecutor();
        taskRecorder = new TaskRecorder();
        commandRecorder = new HistoryCommandRecorder();
        criterionRecorder = new CriterionRecorder();

        task = new Task(
                "Task1",
                "This is task 1",
                2,
                new Task[]{},
                new Task[]{},
                true
        );

        criterion = new Criterion(
                "Criterion1",
                "Property1",
                "Op1",
                1,
                null,
                null,
                null,
                "Basic"
        );

        taskRecorder.addTask(task);
        criterionRecorder.addCriterion(criterion);
    }

    @Test
    public void testExecuteInstruction() throws IOException {
        // Execute the instruction
        Path tempFile = Files.createTempFile(null, null);
        storeExecutor.executeInstruction(new String[]{tempFile.toString()}, taskRecorder, commandRecorder, criterionRecorder);

        // Check the contents of the file
        try (BufferedReader reader = new BufferedReader(new FileReader(tempFile.toFile()))) {
            assertEquals("TASK: ", reader.readLine());
            assertEquals("task: Task1 This is task 1 2.0 , Primitive ,", reader.readLine());
            assertEquals("CRITERION: ", reader.readLine());
            assertEquals("criterion: Criterion1 Basic Property1 Op1 1", reader.readLine());
        }
    }

    @Test
    public void testUndoInstruction() {
        // Given
        String[] parameters = {"parameter1", "parameter2"};
        StoreExecutor storeExecutor = new StoreExecutor();

        // When
        storeExecutor.undoInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);

    }

    @Test
    public void testGetStatus() {
        // Given
        StoreExecutor storeExecutor = new StoreExecutor();

        // When
        boolean status = storeExecutor.getStatus();

        boolean expectedStatus = false;
        assertEquals(expectedStatus, status);
    }
}
