package hk.edu.polyu.comp.comp2021.tms.model;

/**
 * An interface for methods of executing all instructions
 * It provides a method to execute an instruction with the specified parameters and task recorder.
 *
 * @author Wang Ruijie
 * @version 2.0
 */
public interface InstructionExecutor {

    /**
     * Executes an instruction with the specified parameters and task recorder.
     *
     * @param parameters         the parameters for executing the instruction
     * @param taskRecorder       the task recorder used to record and manage tasks
     * @param commandRecorder    the commandRecorder used to record successfully executed commands
     * @param criterionRecorder  the criterionRecorder used to record and mange criteria
     */
    void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder);

    /**
     * Undoes the previous instruction execution.
     *
     * @param parameters       The parameters associated with the instruction.
     * @param taskRecorder     The Task Recorder to update.
     * @param commandRecorder  The Command Recorder to update.
     * @param criterionRecorder The Criterion Recorder to update.
     */
    void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder);

    /**
     * Retrieves the status of the executor.
     *
     * @return The status of the executor.
     */
    boolean getStatus();
}
