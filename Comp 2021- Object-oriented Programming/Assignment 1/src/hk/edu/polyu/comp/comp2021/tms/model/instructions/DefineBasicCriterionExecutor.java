package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;

import java.util.Objects;


/**
 * This class represents an executor for the DefineBasicCriterion instruction.
 * Implements the InstructionExecutor interface.
 *
 * <p>
 * The DefineBasicCriterionExecutor class is responsible for executing and undoing the DefineBasicCriterion instruction.
 * It validates the input parameters, creates a BasicCriterion object, and adds it to the CriterionRecorder.
 * </p>
 *
 * @author Zhu Jin Shun
 * @version 1.0
 *
 * */
public class DefineBasicCriterionExecutor implements InstructionExecutor {

    private boolean status;

    /**
     * Constructs a DefineBasicCriterionExecutor object.
     * The initial status is set to false.
     */
    public DefineBasicCriterionExecutor() {
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
     * Undoes the DefineBasicCriterion instruction.
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
     * Executes the DefineBasicCriterion instruction.
     * Validates the input parameters, creates a BasicCriterion object, and adds it to the CriterionRecorder.
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

        String criterionName = parameters[0];
        String property = parameters[1];
        String op = parameters[2];
        Object value = parameters[3];

        if (Objects.equals(criterionName, "IsPrimitive")) {
            System.out.println("IsPrimitive can not be the name of a criterion");
            return;
        }

        Criterion basicCriterion = new BasicCriterion(criterionName,property, op, value);
        if(isValidBasicCriterion(basicCriterion,criterionRecorder)){
            criterionRecorder.addCriterion(basicCriterion);
            System.out.println("The basic criterion is successfully defined");
            this.status = true;
        }
        else{
            System.out.println("Invalid input or repeated name. Please try again with correct value and format");
        }
    }

    /**
     * A method to check whether the input criterion name is a repeated name.
     *
     * @param name              the criterion name to check
     * @param criterionRecorder the criterion recorder to check against
     * @return true if the criterion name is not repeated, false otherwise
     */
    public boolean isRepeatedCriterionName(String name, CriterionRecorder criterionRecorder){
        return !criterionRecorder.isInRecorder(name);
    }

    /**
     * Checks whether the provided basic criterion is valid.
     *
     * @param basicCriterion     the basic criterion to validate
     * @param criterionRecorder  the criterion recorder to check against
     * @return true if the basic criterion is valid, false otherwise
     */
    public boolean isValidBasicCriterion(Criterion basicCriterion, CriterionRecorder criterionRecorder) {
        String basicCriterionName = basicCriterion.getName();
        String basicProperty = basicCriterion.getProperty();
        String basicOp = basicCriterion.getOp();
        Object basicValue = basicCriterion.getValue();
        return isRepeatedCriterionName(basicCriterionName, criterionRecorder) && Criterion.isValidCriterionName(basicCriterionName) && Criterion.isValidCriterionProperty(basicProperty) && Criterion.isValidCriterionOp(basicProperty, basicOp) && Criterion.isValidCriterionValue(basicProperty, basicOp, basicValue);
    }

}
