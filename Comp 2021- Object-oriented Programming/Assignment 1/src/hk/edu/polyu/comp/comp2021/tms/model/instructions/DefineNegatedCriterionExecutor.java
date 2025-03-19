package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;

import java.util.Objects;

/**
 * This class represents an executor for the DefineNegatedCriterion instruction.
 * Implements the InstructionExecutor interface.
 *
 * @author Zhu Jin Shun
 * @version 1.0
 *
 */
public class DefineNegatedCriterionExecutor implements InstructionExecutor {

    private boolean status;

    /**
     * Constructs a DefineNegatedCriterionExecutor object.
     * The initial status is set to false.
     */
    public DefineNegatedCriterionExecutor() {
        this.status = false;
    }

    /**
     * Gets the status of the executor.
     *
     * @return the status of the executor
     */
    @Override
    public boolean getStatus() {
        return this.status;
    }

    /**
     * Undoes the DefineNegatedCriterion instruction.
     * Deletes the criterion with the specified name from the CriterionRecorder.
     *
     * @param parameters        the parameters of the instruction
     * @param taskRecorder      the task recorder
     * @param commandRecorder   the history command recorder
     * @param criterionRecorder the criterion recorder
     */
    @Override
    public void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {
        criterionRecorder.deleteCriterion(parameters[0]);
    }

    /**
     * Executes the DefineNegatedCriterion instruction.
     * Validates the input parameters, retrieves an existing criterion, creates a NegatedCriterion object,
     * and adds it to the CriterionRecorder.
     *
     * @param parameters               the parameters of the instruction
     * @param taskRecorder             the task recorder
     * @param historyCommandRecorder   the history command recorder
     * @param criterionRecorder        the criterion recorder
     */
    @Override
    public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder historyCommandRecorder, CriterionRecorder criterionRecorder) {

        if (parameters.length != 2) {
            System.out.println("Invalid input parameter");
            return;
        }

        String name1 = parameters[0];
        String name2 = parameters[1];

        if (Objects.equals(name1, name2)) {
            System.out.println("Names repeated. Please provide different criterion names");
            return;
        }

        if (Objects.equals(name1, "IsPrimitive")) {
            System.out.println("IsPrimitive can not be the name of a criterion");
            return;
        }

        Criterion existingCriterion = criterionRecorder.getCriterion(name2);
        if(criterionRecorder.isInRecorder(name1)) {
            System.out.println("Invalid name or repeated. Please provide a valid name");
            return;
        }

        if (existingCriterion == null) {
            System.out.println("Existing criterion not found. Please provide a valid existing criterion name");
            return;
        } else {
            if (Objects.equals(existingCriterion.getType(), "Negated") || Objects.equals(existingCriterion.getType(), "Binary")) {
                System.out.println("The type of the task to be negated can not be composite but only basic");
                return;
            }
        }

        Criterion negatedCriterion = new NegatedCriterion(name1, existingCriterion);
        criterionRecorder.addCriterion(negatedCriterion);
        System.out.println("The negated criterion is successfully defined");
        this.status = true;

    }
}