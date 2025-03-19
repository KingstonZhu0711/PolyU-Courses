package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;

/**
 * This class represents an executor for the "ReportEarliestFinishTime" instruction.
 * It implements the InstructionExecutor interface and provides methods to execute the instruction.
 * The "ReportEarliestFinishTime" instruction calculates and displays the earliest finish time of a specified task.
 * Note: The "ReportEarliestFinishTime" instruction cannot be undone.
 *
 * @author Wang Ruijie
 * @version 2.0
 */
public class ReportEarliestFinishTimeExecutor implements InstructionExecutor {

    private final boolean status;

    /**
     * Constructs a ReportEarliestFinishTimeExecutor object.
     * The initial status is set to false.
     */
    public ReportEarliestFinishTimeExecutor() {
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
     * Does nothing for the "ReportEarliestFinishTime" instruction, as it cannot be undone.
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

        if (!taskRecorder.existsTask(parameters[0])) {
            System.out.println("The input task does not exist");
            return;
        }

        Task reportingTask = taskRecorder.getTask(parameters[0]);
        double EarliestFinishTime = reportingTask.getDuration();

        Task[] directPrerequisite = reportingTask.getDirectPrerequisites();
        for (Task task : directPrerequisite) {
            EarliestFinishTime += task.getDuration();
        }

        Task[] indirectPrerequisite = reportingTask.getIndirectPrerequisites();
        for (Task task : indirectPrerequisite) {
            EarliestFinishTime += task.getDuration();
        }

        System.out.println("--------Earliest Finish Time of " + parameters[0] + "--------");
        System.out.println("The earliest finish time: " + EarliestFinishTime);

    }

    @Override
    public void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {}

}

