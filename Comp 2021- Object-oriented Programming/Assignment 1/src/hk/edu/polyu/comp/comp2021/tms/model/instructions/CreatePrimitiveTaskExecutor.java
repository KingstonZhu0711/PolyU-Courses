package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;

import static hk.edu.polyu.comp.comp2021.tms.model.TaskRecorder.allExist;

/**
 * This class represents an executor for the "CreatePrimitiveTask" instruction.
 * It implements the InstructionExecutor interface and provides methods to execute the instruction.
 *
 * <p>
 *     The "CreatePrimitiveTask" instruction creates a new primitive task with the specified parameters,
 *     including name, description, duration, and prerequisites. The executor validates the input parameters
 *     and adds the new task to the task recorder.
 * </p>
 *
 * <p>
 *     This executor also supports undoing the creation of a primitive task by deleting it from the task recorder.
 * </p>
 *
 * <p>
 *     Note: This executor assumes that the task recorder, history command recorder, and criterion recorder
 *     provided to the executeInstruction() and undoInstruction() methods are not null.
 * </p>
 *
 * @author Wang Ruijie
 * @author Liu Yuyang
 * @version 3.0
 */
public class CreatePrimitiveTaskExecutor implements InstructionExecutor {

    private boolean status;

    /**
     * Constructs a CreatePrimitiveTaskExecutor object with an initial status of false.
     * The status indicates whether the execution of the instruction was successful.
     */
    public CreatePrimitiveTaskExecutor() {
        this.status = false;
    }


    /**
     * Returns the status of the executor, indicating whether the execution of the instruction was successful.
     *
     * @return true if the execution was successful, false otherwise.
     */
    @Override
    public boolean getStatus() {
        return this.status;
    }

    /**
     * Executes the "CreatePrimitiveTask" instruction with the specified parameters.
     *
     * @param parameters       the parameters of the instruction
     * @param taskRecorder     the task recorder to modify
     * @param commandRecorder  the history command recorder to update
     * @param criterionRecorder  the criterion recorder to update
     */
    @Override
    public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

        if (parameters.length != 4) {
            System.out.println("Invalid parameters input");
            return;
        }

        String name = parameters[0];
        if (!Task.isValidName(name)) {
            System.out.println("The input name is not valid");
            return;
        }

        if (taskRecorder.existsTask(parameters[0])) {
            System.out.println("The input task has been already created and repeated");
            return;
        }

        String description = parameters[1];
        if (!Task.isValidDescription(description)) {
            System.out.println("The input description is not valid");
            return;
        }

        if (!Task.isValidDurationInput(parameters[2])) {
            System.out.println("The input duration contains invalid characters");
            return;
        }

        double duration = Double.parseDouble(parameters[2]);
        if (!Task.isValidDuration(duration)) {
            System.out.println("The input duration is not a positive number");
            return;
        }

        if (!Task.isValidPrerequisiteInput(parameters[3])) {
            System.out.println("The input prerequisite is not valid. Your input should be comma-seperated");
            return;
        }

        String[] prerequisiteNames = parameters[3].split(",");
        if (!allExist(prerequisiteNames, taskRecorder)) {
            System.out.println("The input prerequisite(s) has not been created");
            return;
        }

        Task[] prerequisites = Task.getTaskList(prerequisiteNames, taskRecorder);

        Task newSimpleTask = new PrimitiveTask(name, description, duration, prerequisites);
        taskRecorder.addTask(newSimpleTask);
        System.out.println("The primitive task is successfully created");
        this.status = true;

    }

    /**
     * Undoes the execution of the "CreatePrimitiveTask" instruction by deleting the created task from the task recorder.
     *
     * @param parameters       the parameters of the instruction
     * @param taskRecorder     the task recorder to modify
     * @param commandRecorder  the history command recorder to update
     * @param criterionRecorder  the criterion recorder to update
     */
    @Override
    public void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {
        taskRecorder.deleteTask(parameters[0]);
    }

}
