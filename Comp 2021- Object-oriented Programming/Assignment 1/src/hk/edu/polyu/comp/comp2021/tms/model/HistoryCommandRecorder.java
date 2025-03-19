package hk.edu.polyu.comp.comp2021.tms.model;

import java.util.Stack;

/**
 * HistoryCommandRecorder is a class for recording and managing executed and undone commands.
 *
 * @author Wang Ruijie
 * @version 1.0
 */
public class HistoryCommandRecorder {

    private final Stack<CommandRecord> executedCommands;
    private final Stack<CommandRecord> undoneCommands;

    /**
     * Constructs a HistoryCommandRecorder.
     * Initializes the stacks for executed and undone commands.
     */
    public HistoryCommandRecorder() {
        this.executedCommands = new Stack<>();
        this.undoneCommands = new Stack<>();
    }

    /**
     * Clears the history command recorder by removing all executed and undone commands.
     */
    public void clearRecorder() {
        this.executedCommands.clear();
        this.undoneCommands.clear();
    }

    /**
     * Represents a record of an executed command.
     */
    public static class CommandRecord {

        private final InstructionExecutor executor;
        private final String[] parameters;

        /**
         * Constructs a CommandRecord with the specified executor and parameters.
         *
         * @param executor   The instruction executor.
         * @param parameters The parameters associated with the command.
         */
        public CommandRecord(InstructionExecutor executor, String[] parameters) {
            this.executor = executor;
            this.parameters = parameters;
        }

        /**
         * Retrieves the instruction executor associated with the command.
         *
         * @return The instruction executor.
         */
        public InstructionExecutor getInstructionExecutor() {
            return this.executor;
        }

        /**
         * Retrieves the parameters associated with the command.
         *
         * @return The parameters.
         */
        public String[] getParameters() {
            return parameters;
        }
    }

    /**
     * Pushes an executed command to the recorder and clears the undone commands.
     *
     * @param executedCommand The executed command to be recorded.
     */
    public void pushExecuted(CommandRecord executedCommand) {
        this.executedCommands.push(executedCommand);
        this.undoneCommands.clear();
    }

    /**
     * Retrieves the last executed command to be undone.
     * Moves the command from executed commands to undone commands.
     *
     * @return The undoing command.
     */
    public CommandRecord getUndoingCommand() {
        CommandRecord undoingCommand = this.executedCommands.pop();
        this.undoneCommands.push(undoingCommand);
        return undoingCommand;
    }

    /**
     * Retrieves the last undone command to be redone.
     * Moves the command from undone commands to executed commands.
     *
     * @return The redoing command.
     */
    public CommandRecord getRedoingCommand() {
        CommandRecord redoingCommand = this.undoneCommands.pop();
        this.executedCommands.push(redoingCommand);
        return redoingCommand;
    }

    /**
     * Displays the command history, including executed and undone commands.
     */
    public void displayCommandHistory() {
        System.out.println("Executed Commands:");
        System.out.println("------------------");
        for (CommandRecord commandRecord : executedCommands) {
            System.out.println("Executor: " + commandRecord.getInstructionExecutor().getClass().getSimpleName());
            System.out.println("Parameters: " + String.join(", ", commandRecord.getParameters()));
            System.out.println("------------------");
        }

        System.out.println("Undone Commands:");
        System.out.println("----------------");
        for (CommandRecord commandRecord : undoneCommands) {
            System.out.println("Executor: " + commandRecord.getInstructionExecutor().getClass().getSimpleName());
            System.out.println("Parameters: " + String.join(", ", commandRecord.getParameters()));
            System.out.println("----------------");
        }
    }

    /**
     * Checks if there are any commands that can be undone.
     *
     * @return {@code true} if there are commands that can be undone, {@code false} otherwise.
     */
    public boolean canUndo() {
        return !(this.executedCommands.empty());
    }

    /**
     * Checks if there are any commands that can be redone.
     *
     * @return {@code true} if there are commands that can be redone, {@code false} otherwise.
     */
    public boolean canRedo() {
        return !(this.undoneCommands.empty());
    }

    /**
     * Checks if the given instruction executor can be undone based on its status.
     *
     * @param executor The instruction executor to check.
     * @return {@code true} if the executor can be undone, {@code false} otherwise.
     */
    public static boolean executorCanBeUndone(InstructionExecutor executor) {
        return executor.getStatus();
    }

}
