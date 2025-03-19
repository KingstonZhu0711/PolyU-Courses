package hk.edu.polyu.comp.comp2021.tms.model;

// Import necessary classes from JUnit library for tests and assertions
import org.junit.Test;
import static org.junit.Assert.*;

// Import necessary classes for handling InputStream
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * This class represents a test suite for the CommandLineScanner class.
 * It contains methods to test all the functionalities provided by the CommandLineScanner class.
 *
 * @author liuyuyang
 */
public class CommandLineScannerTest {

    /**
     * This test case checks the functionality of CommandLineScanner class.
     * It simulates a user input stream, then checks if the command line read by the scanner matches the user input.
     */
    @Test
    public void testCommandLineScanner() {
        // Simulate a user input stream
        String input = "test command line";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        // Create a new instance of CommandLineScanner
        CommandLineScanner scanner = new CommandLineScanner();

        // Get the command line read by the scanner
        String commandLine = scanner.getCommandLine();

        // Assert that the command line read by the scanner matches the user input
        assertEquals(input, commandLine);
    }
}