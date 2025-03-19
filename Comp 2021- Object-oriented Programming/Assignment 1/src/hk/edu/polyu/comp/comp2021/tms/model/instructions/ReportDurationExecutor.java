package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;

/**
 * This class represents an executor for the "ReportDuration" instruction.
 * It implements the InstructionExecutor interface and provides methods to execute the instruction.
 * Note: The "ReportDuration" instruction cannot be undone.
 *
 * @author Wang Ruijie
 * @version 2.0
 */
public class ReportDurationExecutor implements InstructionExecutor {

    private final boolean status;

    /**
     * Constructs a ReportDurationExecutor object.
     * The initial status is set to false.
     */
    public ReportDurationExecutor() {
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
     * Does nothing for the "ReportDuration" instruction, as it cannot be undone.
     *
     * @param parameters the parameters for the instruction (not used)
     * @param taskRecorder the task recorder instance
     * @param commandRecorder the history command recorder
     * @param criterionRecorder the criterion recorder
     */

    @Override
    public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

        if (parameters.length != 1) {
            System.out.println("Invalid input parameters");
            return;
        }

        Task reportingTask = taskRecorder.getTask(parameters[0]);
        System.out.println("--------Duration of " + parameters[0] + "--------");
        System.out.println("Duration: " + reportingTask.getDuration());

    }

    @Override
    public void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {}

}
