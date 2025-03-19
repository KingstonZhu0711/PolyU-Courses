package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;

/**
 * This class represents an executor for the "CreateCompositeTask" instruction.
 * It implements the InstructionExecutor interface and provides methods to execute the instruction.
 *
 * <p>
 * The CreateCompositeTaskExecutor class is responsible for executing the "CreateCompositeTask" instruction.
 * It validates the input parameters, creates a new composite task, and adds it to the task recorder.
 * </p>
 *
 * @author Wang Ruijie
 * @author Liu Yuyang
 * @version 2.0
 */
public class CreateCompositeTaskExecutor implements InstructionExecutor {

    private boolean status;

    /**
     * Constructs a CreateCompositeTaskExecutor object.
     * Initializes the status to false.
     */
    public CreateCompositeTaskExecutor() {
        this.status = false;
    }

    /**
     * Retrieves the status of the executor.
     *
     * @return the status of the executor
     */
    @Override
    public boolean getStatus() {
        return this.status;
    }

    /**
     * Executes the "CreateCompositeTask" instruction with the specified parameters.
     *
     * @param parameters       the parameters of the instruction
     * @param taskRecorder     the task recorder to modify
     * @param commandRecorder  the command recorder to record the executed command
     * @param criterionRecorder the criterion recorder to modify
     */
    @Override
    public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

        if (parameters.length != 3) {
            System.out.println("Invalid parameters input");
            return;
        }

        String name = parameters[0];
        if (!Task.isValidName(name)) {
            System.out.println("The input name is not valid");
            return;
        }

        if (taskRecorder.existsTask(name)) {
            System.out.println("The input task has been created and repeated");
            return;
        }

        String description = parameters[1];
        if (!Task.isValidDescription(description)) {
            System.out.println("The input description is not valid");
            return;
        }

        String subtasks = parameters[2];
        if (!Task.isValidSubtaskInput(subtasks)) {
            System.out.println("The input subtask is not valid. Your input should be comma-seperated");
            return;
        }

        String[] subTasks = parameters[2].split(",");
        Task[] subTaskList = Task.getTaskList(subTasks, taskRecorder);
        if(!isValidSubTaskList(subTaskList, taskRecorder)) {
            System.out.println("The input subtask(s) has not created");
            return;
        }

        Task newCompositeTask = new CompositeTask(name, description, subTaskList);
        taskRecorder.addTask(newCompositeTask);
        System.out.println("The composite task is successfully created");
        this.status = true;

    }

    /**
     * Undoes the "CreateCompositeTask" instruction by deleting the created composite task.
     *
     * @param parameters       the parameters of the instruction
     * @param taskRecorder     the task recorder to modify
     * @param commandRecorder  the command recorder to record the executed command
     * @param criterionRecorder the criterion recorder to modify
     */
    @Override
    public void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {
        taskRecorder.deleteTask(parameters[0]);
    }

    /**
     * Checks if the given subTaskList is a valid list of subtasks.
     *
     * @param subTaskList    the list of subtasks to check
     * @param taskRecorder   the task recorder to check the existence of subtasks
     * @return true if the subTaskList is valid, false otherwise
     */
    public static boolean isValidSubTaskList(Task[] subTaskList, TaskRecorder taskRecorder) {
        for (Task subTask : subTaskList) {
            try {
                taskRecorder.existsTask(subTask.getName());
            } catch (NullPointerException e) {
                return false;
            }
        }
        return true;
    }

}
