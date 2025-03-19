package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.CriterionRecorder;
import hk.edu.polyu.comp.comp2021.tms.model.HistoryCommandRecorder;
import hk.edu.polyu.comp.comp2021.tms.model.InstructionExecutor;
import hk.edu.polyu.comp.comp2021.tms.model.TaskRecorder;

/**
 * This class represents an executor for the "Quit" instruction.
 * It implements the InstructionExecutor interface and provides methods to execute the instruction.
 * When executed, it terminates the program.
 * Note: The "Quit" instruction cannot be undone.
 *
 * @author Wang Ruijie
 * @version 1.0
 */
public class QuitExecutor implements InstructionExecutor {

    /**
     * Executes the "Quit" instruction.
     * Terminates the program.
     *
     * @param parameters the parameters for the instruction (not used)
     * @param taskRecorder the task recorder instance
     * @param commandRecorder the history command recorder
     * @param criterionRecorder the criterion recorder
     */
    @Override
    public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {
        System.exit(0);
    }

    @Override
    public void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {}

    /**
     * Get the status
     * @return false
     */
    @Override
    public boolean getStatus() {
        return false;
    }

}
