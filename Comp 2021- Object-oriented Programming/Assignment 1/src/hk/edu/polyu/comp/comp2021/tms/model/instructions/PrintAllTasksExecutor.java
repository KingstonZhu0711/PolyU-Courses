package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.CriterionRecorder;
import hk.edu.polyu.comp.comp2021.tms.model.HistoryCommandRecorder;
import hk.edu.polyu.comp.comp2021.tms.model.InstructionExecutor;
import hk.edu.polyu.comp.comp2021.tms.model.TaskRecorder;

/**
 * This class represents an executor for the "PrintAllTasks" instruction.
 * It implements the InstructionExecutor interface and provides methods to execute the instruction.
 *
 * @author Wang Ruijie
 *
 * @version 1.0
 */
public class PrintAllTasksExecutor implements InstructionExecutor {

    private final boolean status;

    /**
     * Construct the executor.
     * the status is set to be false。
     */
    public PrintAllTasksExecutor() {
        this.status = false;
    }

    /**
     * Return the status of the executor.
     *
     * @return false always
     */
    @Override
    public boolean getStatus() {
        return this.status;
    }

    @Override
    public void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {}

    /**
     * Executes the "PrintAllTasks" instruction with the specified parameters.
     *
     * @param parameters      the parameters of the instruction
     * @param taskRecorder    the task recorder to access tasks from
     * @param commandRecorder the history command recorder
     * @param criterionRecorder the criterion recorder
     */
    @Override
    public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

        if (parameters.length != 0) {
            /*Exception?*/
            return;
        }

        taskRecorder.showAllTasks();

    }

}
