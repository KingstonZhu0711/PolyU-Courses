package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.CriterionRecorder;
import hk.edu.polyu.comp.comp2021.tms.model.HistoryCommandRecorder;
import hk.edu.polyu.comp.comp2021.tms.model.InstructionExecutor;
import hk.edu.polyu.comp.comp2021.tms.model.TaskRecorder;

/**
 * This class represents an executor for the "PrintTask" instruction.
 * It implements the InstructionExecutor interface and provides methods to execute the instruction.
 *
 * @author Wang Ruijie
 *
 * @version 1.0
 */
public class PrintTaskExecutor implements InstructionExecutor {

    private final boolean status;

   /**
    * Constructs a PrintTaskExecutor object.
    * The initial status is set to false
    */
    public PrintTaskExecutor() {
        this.status = false;
    }

    /**
     * Return the status.
     *
     * @return always false
     */
    @Override
    public boolean getStatus() {
        return this.status;
    }

    /**
     * Executes the "PrintTask" instruction with the specified parameters.
     *
     * @param parameters the parameters of the instruction
     * @param taskRecorder the task recorder to access tasks from
     * @param commandRecorder the history command recorder
     * @param criterionRecorder the criterion recorder
     */
    @Override
    public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

        if (parameters.length != 1) {
            return;
            /*Exception*/
        }

        else if (!taskRecorder.existsTask(parameters[0])) {
            return;
            /*Exception*/
        }

        taskRecorder.showTask(parameters[0]);

    }

    @Override
    public void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {}

}
