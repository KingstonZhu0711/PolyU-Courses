package hk.edu.polyu.comp.comp2021.tms.model;

import java.util.Scanner;

/**
 * This class represents a command line scanner that reads input from the user.
 * It provides a method to retrieve the entered command line.
 *
 * @author Wang Ruijie
 * @version 1.0
 */
public class CommandLineScanner {

    private String commandLine;

    /**
     * Constructs a CommandLineScanner object and reads the command line from the user.
     */
    public CommandLineScanner() {
        Scanner commandLine = new Scanner(System.in);
        if (commandLine.hasNextLine()) {
            this.commandLine = commandLine.nextLine();
        }
    }

    /**
     * Returns the command line entered by the user.
     *
     * @return the command line entered by the user
     */
    public String getCommandLine() {
        return this.commandLine;
    }

}



