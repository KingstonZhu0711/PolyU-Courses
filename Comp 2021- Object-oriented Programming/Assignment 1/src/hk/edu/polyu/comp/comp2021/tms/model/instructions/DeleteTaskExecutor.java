package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;

import java.util.ArrayList;
import java.util.List;

import static hk.edu.polyu.comp.comp2021.tms.model.Task.contains;

/**
 * Implementation of the InstructionExecutor interface for executing the "delete" instruction.
 * This executor deletes a task and its subtasks if applicable, after performing prerequisite checks.
 *
 * @author Wang Ruijie
 *
 * @version 2.0
 */
public class DeleteTaskExecutor implements InstructionExecutor {

    private boolean status;

    private final List<Task> deletedTasks;

    /**
     * Construct the deleteTask executor
     * Set the initial status to be false and the deletedTask list to be empty
     */
    public DeleteTaskExecutor() {
        this.status = false;
        this.deletedTasks = new ArrayList<>();
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
     * Executes the "delete" instruction with the provided parameters.
     *
     * @param parameters the parameters for the instruction
     * @param taskRecorder the task recorder instance to access and modify tasks
     * @param commandRecorder the history command recorder
     * @param criterionRecorder the criterion recorder
     */
    @Override
    public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

        if (parameters.length != 1) {
            return;
            /*Exception*/
        }

        if (!taskRecorder.existsTask(parameters[0])) {
            return;
            /*Exception*/
        }

        Task deletingTask = taskRecorder.getTask(parameters[0]);
        Task[] allExistingTask = taskRecorder.getAllTasks();

        for (Task existingTask : allExistingTask) {
            if (contains(existingTask.getDirectPrerequisites(), deletingTask) || contains(existingTask.getIndirectPrerequisites(), deletingTask)) {
                System.out.println("The deleting task is a prerequisite of a existing task");
                return;
            }

            if (!subtaskCheck(allExistingTask, deletingTask.getSubTasks())) {
                System.out.println("At least one of the subtasks of the deleting task is a prerequisite of a existing task");
                return;
            }

            if (contains(existingTask.getSubTasks(), deletingTask)) {
                System.out.println("The deleting task is a subtask of at least one composite task");
                return;
            }

            this.deletedTasks.add(deletingTask);
            collectSubtasks(deletingTask, this.deletedTasks);

            deleteSubtasks(deletingTask, taskRecorder);
            taskRecorder.deleteTask(deletingTask.getName());
            this.status = true;
        }

        System.out.println(deletingTask.getName() + " is successfully deleted");

    }

    /**
     * Checks if the subtasks of the deleting task are prerequisites of any existing task.
     *
     * @param allExistingTask the array of all existing tasks
     * @param subtasks the array of subtasks to check
     * @return true if the subtasks are not prerequisites of any existing task, false otherwise
     */
    public static boolean subtaskCheck(Task[] allExistingTask, Task[] subtasks) {
        for (Task subtask: subtasks) {
            if (subtask.getSubTasks().length == 0) {
                for (Task existingTask : allExistingTask) {
                    if (contains(existingTask.getDirectPrerequisites(), subtask) || contains(existingTask.getIndirectPrerequisites(), subtask)) {
                        return false;
                    }
                }
            } else {
                subtaskCheck(allExistingTask, subtask.getSubTasks());
            }
        }
        return true;
    }

    /**
     * Deletes the subtasks of the given task recursively.
     *
     * @param task the task to delete the subtasks from
     * @param taskRecorder the task recorder instance
     */
    public void deleteSubtasks(Task task, TaskRecorder taskRecorder) {
        Task[] subtasks = task.getSubTasks();
        for (Task subtask : subtasks) {
            if (subtask.getSubTasks().length == 0) {
                taskRecorder.deleteTask(subtask.getName());
                //System.out.println(subtask.getName() + " is successfully deleted");
            } else {
                deleteSubtasks(subtask, taskRecorder);
            }
        }
    }

    /**
     * Collects all the subtasks of the given task recursively.
     *
     * @param task the task to collect the subtasks from
     * @param subtasks the list to store the collected subtasks
     */
    private void collectSubtasks(Task task, List<Task> subtasks) {
        for (Task subtask : task.getSubTasks()) {
            subtasks.add(subtask);
            collectSubtasks(subtask, subtasks);
        }
    }

    /**
     * Restores the subtasks of the given task recursively.
     *
     * @param task the task to restore the subtasks to
     * @param taskRecorder the task recorder instance
     */
    private void restoreSubtasks(Task task, TaskRecorder taskRecorder) {
        for (Task subtask : task.getSubTasks()) {
            taskRecorder.addTask(subtask);
            restoreSubtasks(subtask, taskRecorder);
        }
    }

    /**
     * Undoes the DefineNegatedCriterion instruction.
     * Add the deleted task into the taskRecorder again.
     *
     * @param parameters        the parameters of the instruction
     * @param taskRecorder      the task recorder
     * @param commandRecorder   the history command recorder
     * @param criterionRecorder the criterion recorder
     */
    @Override
    public void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {
        for (Task task : this.deletedTasks) {
            taskRecorder.addTask(task);
            restoreSubtasks(task, taskRecorder);
        }
        this.deletedTasks.clear();
    }

}
