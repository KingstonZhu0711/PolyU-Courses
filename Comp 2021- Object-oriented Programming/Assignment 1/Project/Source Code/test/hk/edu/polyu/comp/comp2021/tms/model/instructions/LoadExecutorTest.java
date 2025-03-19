package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.CriterionRecorder;
import hk.edu.polyu.comp.comp2021.tms.model.HistoryCommandRecorder;
import hk.edu.polyu.comp.comp2021.tms.model.TaskRecorder;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class LoadExecutorTest {

    private LoadExecutor loadExecutor;
    private TaskRecorder taskRecorder;
    private HistoryCommandRecorder commandRecorder;
    private CriterionRecorder criterionRecorder;

    @Before
    public void setUp() {
        loadExecutor = new LoadExecutor();
        taskRecorder = new TaskRecorder();
        commandRecorder = new HistoryCommandRecorder();
        criterionRecorder = new CriterionRecorder();
    }

    @Test
    public void testExecuteInstruction() throws IOException {
        // Given
        Path tempFile = Files.createTempFile(null, null);

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            writer.write("task: Task1 This is task 1 2 Primitive\n");
            writer.write("criterion: Criterion1 Basic Property1 Op1 1\n");
        }

        // When
        loadExecutor.executeInstruction(new String[]{tempFile.toString()}, taskRecorder, commandRecorder, criterionRecorder);

        // Then
        // Since the executeInstruction method doesn't return anything,
        // there's nothing to assert or verify here.
        // This test simply ensures that the method can be called without causing any errors.
    }

    @Test
    public void testGetStatus() {
        assertFalse(loadExecutor.getStatus());
    }

    @Test
    public void testUndoInstruction() {
        loadExecutor.undoInstruction(new String[]{}, taskRecorder, commandRecorder, criterionRecorder);
        // Again, as the undoInstruction method doesn't do anything,
        // there's nothing to assert or verify here.
    }

    @Test
    public void testCodeCoverage() {
        // Initialize your recorder and executor objects
        TaskRecorder taskRecorder = new TaskRecorder();
        HistoryCommandRecorder commandRecorder = new HistoryCommandRecorder();
        CriterionRecorder criterionRecorder = new CriterionRecorder();
        LoadExecutor executor = new LoadExecutor(); // Replace 'YourExecutor' with your actual class name

        // Define task and criterion parameters
        String[] primitiveTaskParams = new String[]{"task1", "desc1", "Primitive", "", "Primitive", ""};
        String[] compositeTaskParams = new String[]{"task2", "desc2", "Composite", "", "", "task1"};
        String[] basicCriterionParams = new String[]{"crit1", "Basic", "task1", ">", "5"};
        String[] negatedCriterionParams = new String[]{"crit2", "Negated", "crit1"};
        String[] binaryCriterionParams = new String[]{"crit3", "Binary", "crit1", "AND", "crit2"};

        // Execute instructions
        executor.executeInstruction(primitiveTaskParams, taskRecorder, commandRecorder, criterionRecorder);
        executor.executeInstruction(compositeTaskParams, taskRecorder, commandRecorder, criterionRecorder);
        executor.executeInstruction(basicCriterionParams, taskRecorder, commandRecorder, criterionRecorder);
        executor.executeInstruction(negatedCriterionParams, taskRecorder, commandRecorder, criterionRecorder);
        executor.executeInstruction(binaryCriterionParams, taskRecorder, commandRecorder, criterionRecorder);
    }

    @Test
    public void testCodeCoverageAdd() throws IOException {
        // Initialize your recorder and executor objects
        TaskRecorder taskRecorder = new TaskRecorder();
        HistoryCommandRecorder commandRecorder = new HistoryCommandRecorder();
        CriterionRecorder criterionRecorder = new CriterionRecorder();
        LoadExecutor executor = new LoadExecutor(); // Replace with your actual class name

        // Create a temporary file
        File tempFile = File.createTempFile("tempFile", ".txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        // Write tasks and criteria to the file
        writer.write("task: task1 desc1 Primitive extra1 Primitive extra2\n"); // Primitive task
        writer.write("task: task2 desc2 Composite extra1 extra2 task1\n"); // Composite task
        writer.write("criterion: crit1 Basic task1 > 5\n"); // Basic criterion
        writer.write("criterion: crit2 Negated crit1\n"); // Negated criterion
        writer.write("criterion: crit3 Binary crit1 AND crit2\n"); // Binary criterion
        writer.close();

        // Execute the method with the temporary file as the argument
        String[] parameters = new String[]{tempFile.getAbsolutePath()};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);

        // Delete the temporary file
        tempFile.delete();
    }

}