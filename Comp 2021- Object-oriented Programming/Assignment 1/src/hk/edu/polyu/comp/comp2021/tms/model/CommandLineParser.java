package hk.edu.polyu.comp.comp2021.tms.model;

/**
 * This class represents a command line parser that parses a command line into an instruction and its parameters.
 * The command line should be in the format: "instruction parameter1 parameter2 ..."
 * The instruction is the first element in the command line, and the parameters are the remaining elements.
 *
 * @author Wang Ruijie
 * @version 1.0
 */
public class CommandLineParser {

    private final String instruction; // The parsed instruction
    private final String[] parameters; // The parsed parameters

    /**
     * Constructs a CommandLineParser object with the given command line.
     *
     * @param commandLine the command line to parse
     */
    public CommandLineParser(String commandLine) {
        String[] commands = commandLine.split(" "); // Split the command line by spaces
        this.instruction = commands[0]; // The first element is the instruction
        this.parameters = new String[commands.length - 1]; // The remaining elements are parameters
        System.arraycopy(commands, 1, this.parameters, 0, commands.length - 1); // Copy the parameters to the array
    }

    /**
     * Returns the parsed instruction.
     *
     * @return the parsed instruction
     */
    public String getInstruction() {
        return this.instruction;
    }

    /**
     * Returns the parsed parameters.
     *
     * @return the parsed parameters
     */
    public String[] getParameters() {
        return this.parameters;
    }

}
