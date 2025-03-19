package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.CriterionRecorder;
import hk.edu.polyu.comp.comp2021.tms.model.HistoryCommandRecorder;
import hk.edu.polyu.comp.comp2021.tms.model.InstructionExecutor;
import hk.edu.polyu.comp.comp2021.tms.model.TaskRecorder;

/**
 * UndoExecutor is an implementation of the InstructionExecutor interface
 * that performs the undo functionality for instructions.
 *
 * @author Wang Ruijie
 * @version 1.0
 */
public class UndoExecutor implements InstructionExecutor {

    private final boolean status;

    /**
     * Construct the undo executor
     * Set the status to be false
     */
    public UndoExecutor() {
        this.status = false;
    }

    @Override
    public boolean getStatus() {
        return this.status;
    }

    @Override
    public void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {}

    /**
     * Performs the undo instruction by invoking the undoInstruction method of the corresponding executor.
     * Retrieves the previous command from the commandRecorder and executes its undo instruction.
     *
     * @param parameters         The parameters associated with the instruction.
     * @param taskRecorder       The Task Recorder to update.
     * @param commandRecorder    The Command Recorder to update.
     * @param criterionRecorder  The Criterion Recorder to update.
     */
    @Override
    public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

        if (!commandRecorder.canUndo()) {
            return;
        }

        HistoryCommandRecorder.CommandRecord undoingCommand = commandRecorder.getUndoingCommand();
        InstructionExecutor undoingExecutor = undoingCommand.getInstructionExecutor();
        String[] undoingParameters = undoingCommand.getParameters();
        undoingExecutor.undoInstruction(undoingParameters, taskRecorder, commandRecorder, criterionRecorder);

    }
}
