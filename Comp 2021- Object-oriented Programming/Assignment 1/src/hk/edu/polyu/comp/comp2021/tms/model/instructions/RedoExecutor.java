package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.CriterionRecorder;
import hk.edu.polyu.comp.comp2021.tms.model.HistoryCommandRecorder;
import hk.edu.polyu.comp.comp2021.tms.model.InstructionExecutor;
import hk.edu.polyu.comp.comp2021.tms.model.TaskRecorder;

/**
 * This class represents an executor for the "Redo" instruction.
 * It implements the InstructionExecutor interface and provides methods to execute the instruction.
 * The "Redo" instruction re-executes the previously undone command.
 * Note: The "Redo" instruction cannot be undone.
 *
 * @author Wang Ruijie
 * @version 1.0
 */
public class RedoExecutor implements InstructionExecutor {

    private final boolean status;

    /**
     * Constructs a RedoExecutor object.
     * The initial status is set to false.
     */
    public RedoExecutor() {
        this.status = false;
    }

    /**
     * Gets the status of the executor.
     * @return the status of the executor
     */
    @Override
    public boolean getStatus() {
        return this.status;
    }

    /**
     * Does nothing for the "Redo" instruction, as it cannot be undone.
     *
     * @param parameters the parameters for the instruction (not used)
     * @param taskRecorder the task recorder instance
     * @param commandRecorder the history command recorder
     * @param criterionRecorder the criterion recorder
     */
    @Override
    public void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {}

    /**
     * Executes the "Redo" instruction.
     * Re-executes the previously undone command.
     *
     * @param parameters the parameters for the instruction (not used)
     * @param taskRecorder the task recorder instance
     * @param commandRecorder the history command recorder
     * @param criterionRecorder the criterion recorder
     */
    @Override
    public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

        if (!commandRecorder.canRedo()) {
            return;
        }

        HistoryCommandRecorder.CommandRecord redoingCommand = commandRecorder.getRedoingCommand();
        InstructionExecutor redoingExecutor = redoingCommand.getInstructionExecutor();
        String[] redoingParameters = redoingCommand.getParameters();
        redoingExecutor.executeInstruction(redoingParameters, taskRecorder, commandRecorder, criterionRecorder);

    }
}
