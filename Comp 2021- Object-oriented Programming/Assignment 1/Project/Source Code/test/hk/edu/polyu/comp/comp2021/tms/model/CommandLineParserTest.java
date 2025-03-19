package hk.edu.polyu.comp.comp2021.tms.model;

// Import necessary classes from JUnit library for tests and assertions
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This class represents a test suite for the CommandLineParser class.
 * It contains methods to test all the functionalities provided by the CommandLineParser class.
 *
 * @author liuyuyang
 */
public class CommandLineParserTest {

    /**
     * This test case checks the functionality of CommandLineParser class.
     * It creates an instance of CommandLineParser with a predefined command line string,
     * then checks if the instruction and parameters parsed by the object match the predefined values.
     */
    @Test
    public void CommandLineParser() {
        // Predefined command line string for testing
        String commandLine = "instruction param1 param2 param3";

        // Create a new instance of CommandLineParser with the predefined command line string
        CommandLineParser parser = new CommandLineParser(commandLine);

        // Define expected values
        String expectedInstruction = "instruction";
        String[] expectedParameters = {"param1", "param2", "param3"};

        // Get actual values from the parser
        String actualInstruction = parser.getInstruction();
        String[] actualParameters = parser.getParameters();

        // Assert that the instruction parsed by the parser matches the expected instruction
        assertEquals(expectedInstruction, actualInstruction);

        // Assert that the parameters parsed by the parser match the expected parameters
        assertArrayEquals(expectedParameters, actualParameters);
    }

}