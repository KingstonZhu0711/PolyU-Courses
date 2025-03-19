package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;
import java.util.Objects;

/**
 * This class represents an executor for the DefineBinaryCriterion instruction.
 * Implements the InstructionExecutor interface.
 *
 * @author Zhu Jin Shun
 * @author Wang Ruijie
 *
 * @version 2.0
 */
public class DefineBinaryCriterionExecutor implements InstructionExecutor {

    private boolean status;

    /**
     * Constructs a DefineBinaryCriterionExecutor object.
     * The initial status is set to false.
     */
    public DefineBinaryCriterionExecutor() {
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
     * Undoes the DefineBinaryCriterion instruction.
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
     * Executes the DefineBinaryCriterion instruction.
     * Validates the input parameters, retrieves existing criteria, creates a BinaryCriterion object,
     * and adds it to the CriterionRecorder.
     *
     * @param parameters        the parameters of the instruction
     * @param taskRecorder      the task recorder
     * @param commandRecorder   the history command recorder
     * @param criterionRecorder the criterion recorder
     */
    @Override
    public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

        if (parameters.length != 4) {
            System.out.println("Invalid parameters input");
            return;
        }

        String name1 = parameters[0];
        String name2 = parameters[1];
        String logicOp = parameters[2];
        String name3 = parameters[3];

        if (Objects.equals(name1, "IsPrimitive")) {
            System.out.println("IsPrimitive can not be the name of a criterion");
            return;
        }

        if (Objects.equals(name1, name2) || Objects.equals(name1, name3) || Objects.equals(name2, name3)) {
            System.out.println("Names repeated. Please provide different criterion names");
            return;
        }

        Criterion existingCriterion1 = criterionRecorder.getCriterion(name2);
        Criterion existingCriterion2 = criterionRecorder.getCriterion(name3);

        if (existingCriterion1 == null || existingCriterion2 == null) {
            System.out.println("Existing criterion not found. Please provide valid existing criterion names");
            return;
        }

        if (!isValidLogicOperator(logicOp)) {
            System.out.println("Invalid logic operator. Please provide a valid logic operator (&& or ||)");
            return;
        }
        else{
            if(logicOp.equals("||")){
                logicOp="or";
            }
            else if(logicOp.equals("&&")){
                logicOp="and";
            }
        }
        if(criterionRecorder.isInRecorder(name1)) {
            System.out.println("Invalid name or repeated. Please provide a valid name");
            return;
        }

        Criterion binaryCriterion = new BinaryCriterion(name1,existingCriterion1,existingCriterion2,logicOp);
        criterionRecorder.addCriterion(binaryCriterion);
        System.out.println("The binary criterion is successfully defined");
        this.status = true;

    }
    /**
     * Checks whether the provided logic operator is valid.
     *
     * @param operator the logic operator to check
     * @return true if the logic operator is valid, false otherwise
     */
    private boolean isValidLogicOperator(String operator) {
        return operator.equals("&&") || operator.equals("||");
    }
}
