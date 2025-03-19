package hk.edu.polyu.comp.comp2021.tms.model;

import java.util.*;

import static hk.edu.polyu.comp.comp2021.tms.model.Task.contains;


/**
 * This class represents a task recorder in the task management system.
 * It provides methods to add, retrieve, delete, and display tasks in the task recorder.
 *
 * @author Wang Ruijie
 * @version 2.0
 */
public class TaskRecorder {

    private final Map<String, Task> taskMap;

    /**
     * Constructs a TaskRecorder object with an empty task map.
     */
    public TaskRecorder() {
        taskMap = new LinkedHashMap<>();
    }

    /**
     * Retrieves all tasks in the Task Recorder.
     *
     * @return An array of all tasks.
     */
    public Task[] getAllTasks() {
        List<Task> allTasks = new ArrayList<>(taskMap.values());
        return allTasks.toArray(new Task[0]);
    }

    /**
     * Retrieves the task with the specified task name from the task recorder.
     *
     * @param taskName the name of the task to be retrieved
     * @return the task with the specified task name, or null if the task does not exist
     */
    public Task getTask(String taskName) {
        return taskMap.get(taskName);
    }

    /**
     * Adds a task to the task recorder.
     *
     * @param task the task to be added
     */
    public void addTask(Task task) {
        taskMap.put(task.getName(), task);
    }

    /**
     * Deletes a task with the specified task name from the task recorder.
     *
     * @param taskName the name of the task to be deleted
     */
    public void deleteTask(String taskName) {
        taskMap.remove(taskName);
    }

    /**
     * Checks if a task with the specified task name exists in the task recorder.
     *
     * @param taskName the name of the task to be checked
     * @return true if the task exists, false otherwise
     */
    public boolean existsTask(String taskName) {
        return taskMap.containsKey(taskName);
    }

    /**
     * Updates the name of a task.
     *
     * @param oldTaskName The old name of the task.
     */
    public void updateTaskName(String oldTaskName) {
        // Assume that the new task name is valid
        // Also assume that the name of the updating task has already been updated.
        Task updatingTask = getTask(oldTaskName);
        deleteTask(oldTaskName);
        addTask(updatingTask);
    }

    /**
     * Checks if all tasks with the given names exist in the Task Recorder.
     *
     * @param names        An array of task names to check.
     * @param taskRecorder The Task Recorder to check against.
     * @return {@code true} if all tasks exist, {@code false} otherwise.
     */
    public static boolean allExist(String[] names, TaskRecorder taskRecorder) {
        for (String name : names) {
            if (!taskRecorder.existsTask(name)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Clears the Task Recorder by removing all tasks.
     */
    public void clearRecorder() {
        this.taskMap.clear();
    }

    /**
     * Displays all tasks in the task recorder.
     */
    public void showAllTasks() {
        for (String taskName : taskMap.keySet()) {
            showTask(taskName);
        }
    }

    /**
     * Displays the information of a task with the specified task name.
     *
     * @param taskName the name of the task to be displayed
     */
    public void showTask(String taskName) {

        Task showingTask = getTask(taskName);
        System.out.println();
        System.out.println("--------Information for " + taskName + "--------");
        System.out.println("Task Name: " + taskName);
        System.out.println("Description: " + showingTask.getDescription());
        Task[] directPrerequisites = showingTask.getDirectPrerequisites();
        Task[] indirectPrerequisites = showingTask.getIndirectPrerequisites();
        System.out.println("Duration: " + showingTask.getDuration());

        if (showingTask.getType()) {
            System.out.println("Type: Primitive");
            System.out.print("Direct Prerequisites: ");
            for (Task prerequisite : directPrerequisites) {

                //something wrong here
                if (prerequisite == null) {
                    System.out.print("");
                } else {
                    System.out.print(prerequisite.getName() + " ");
                }
            }

            System.out.println();
            System.out.print("Indirect Prerequisites: ");
            for (Task prerequisite_ : indirectPrerequisites) {
                System.out.print(prerequisite_.getName() + " ");
            }
        }

        if (!showingTask.getType()) {
            System.out.println("Type: Composite");
            StringBuilder subTaskNameList = new StringBuilder();
            StringBuilder directPrerequisiteNameList = new StringBuilder();
            for (Task subTask : showingTask.getSubTasks()) {
                subTaskNameList.append(subTask.getName());
                subTaskNameList.append(" ");
                for (Task subTaskPrerequisite : subTask.getDirectPrerequisites()) {
                    if (!contains(showingTask.getSubTasks(), subTaskPrerequisite)) {
                        directPrerequisiteNameList.append(subTaskPrerequisite.getName());
                        directPrerequisiteNameList.append(" ");
                    }
                }
            }

            if (directPrerequisiteNameList.toString().isEmpty()) {
                System.out.println("Direct Prerequisite: " + "no direct prerequisite");
            } else {
                System.out.println("Direct Prerequisite: " + directPrerequisiteNameList);
            }

            System.out.print("Indirect Prerequisites: ");
            for (Task prerequisite_ : indirectPrerequisites) {
                System.out.print(prerequisite_.getName() + " ");
            }
            System.out.println();

            System.out.println("Subtasks: " + subTaskNameList);

        }

        System.out.println();

    }
}

