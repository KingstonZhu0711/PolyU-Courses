package hk.edu.polyu.comp.comp2021.tms.model;

import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

/**
 * This class represents a test suite for the HistoryCommandRecorder class.
 * It contains methods to test all the functionalities provided by the HistoryCommandRecorder class.
 *
 * @author liuyuyang
 */
public class HistoryCommandRecorderTest {

    private HistoryCommandRecorder recorder;
    private HistoryCommandRecorder.CommandRecord record1;
    private HistoryCommandRecorder.CommandRecord record2;
    private InstructionExecutor executor1;
    private InstructionExecutor executor2;

    /**
     * Setup method is run before each test and initializes the recorder and two command records for testing.
     */
    @Before
    public void setUp() {
        // Initialize objects for testing
        recorder = new HistoryCommandRecorder();
        executor1 = new InstructionExecutor() {
            // Override methods for testing
            @Override
            public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

            }

            @Override
            public void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

            }

            @Override
            public boolean getStatus() {
                return true;
            }
        };
        executor2 = new InstructionExecutor() {
            // Override methods for testing
            @Override
            public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

            }

            @Override
            public void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

            }

            @Override
            public boolean getStatus() {
                return false;
            }
        };
        record1 = new HistoryCommandRecorder.CommandRecord(executor1, new String[]{"param1"});
        record2 = new HistoryCommandRecorder.CommandRecord(executor2, new String[]{"param2"});
    }

    /**
     * Tests the clearRecorder method of the HistoryCommandRecorder class.
     */
    @Test
    public void testClearRecorder() {
        recorder.pushExecuted(record1);
        recorder.clearRecorder();
        assertFalse(recorder.canUndo());
        assertFalse(recorder.canRedo());
    }

    /**
     * Tests the pushExecuted method of the HistoryCommandRecorder class.
     */
    @Test
    public void testPushExecuted() {
        recorder.pushExecuted(record1);
        assertTrue(recorder.canUndo());
        assertFalse(recorder.canRedo());
    }

    /**
     * Tests the getUndoingCommand method of the HistoryCommandRecorder class.
     */
    @Test
    public void testGetUndoingCommand() {
        recorder.pushExecuted(record1);
        HistoryCommandRecorder.CommandRecord undoingCommand = recorder.getUndoingCommand();
        assertEquals(record1, undoingCommand);
        assertFalse(recorder.canUndo());
        assertTrue(recorder.canRedo());
    }

    /**
     * Tests the getRedoingCommand method of the HistoryCommandRecorder class.
     */
    @Test
    public void testGetRedoingCommand() {
        recorder.pushExecuted(record1);
        recorder.getUndoingCommand();
        HistoryCommandRecorder.CommandRecord redoingCommand = recorder.getRedoingCommand();
        assertEquals(record1, redoingCommand);
        assertTrue(recorder.canUndo());
        assertFalse(recorder.canRedo());
    }

    /**
     * Tests the executorCanBeUndone method of the HistoryCommandRecorder class.
     */
    @Test
    public void testExecutorCanBeUndone() {
        assertTrue(HistoryCommandRecorder.executorCanBeUndone(executor1));
        assertFalse(HistoryCommandRecorder.executorCanBeUndone(executor2));
    }

    /**
     * Tests the displayCommandHistory method of the HistoryCommandRecorder class.
     * It creates a simulated command history and captures the output of the method to verify it's correct.
     */
    @Test
    public void testDisplayCommandHistory() throws Exception {
        recorder.pushExecuted(record1);
        recorder.pushExecuted(record2);
        recorder.getUndoingCommand();

        // Redirect System.out to a ByteArrayOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(baos));

        // Run the method
        recorder.displayCommandHistory();

        // Reset System.out to its original behavior
        System.setOut(originalOut);

        String output = baos.toString();  // The captured output

        String expectedOutput =
                "Executed Commands:\r\n" +
                        "------------------\r\n" +
                        "Executor: " + executor2.getClass().getSimpleName() + "\r\n" +
                        "Parameters: param1\r\n" +
                        "------------------\r\n" +
                        "Undone Commands:\r\n" +
                        "----------------\r\n" +
                        "Executor: " + executor1.getClass().getSimpleName() + "\r\n" +
                        "Parameters: param2\r\n" +
                        "----------------\r\n";

        assertEquals(expectedOutput, output);
    }
}
