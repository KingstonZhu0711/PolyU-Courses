package hk.edu.polyu.comp.comp2021.tms.model;

import java.util.*;


/**
 * This class represents a task in the task management system.
 * It provides methods to access and manipulate task properties, as well as static utility methods for task validation and manipulation.
 *
 * @author Wang Ruijie
 * @author Liu Yuyang
 * @version 3.0
 */
public class Task {

    /**
     * The name of the task.
     */
    private String name;

    /**
     * The description of the task.
     */
    private String description;

    /**
     * The duration of the task.
     */
    protected double duration;

    /**
     * An array of direct prerequisite tasks for this task.
     */
    protected Task[] directPrerequisites;

    /**
     * An array of indirect prerequisite tasks for this task.
     */
    protected Task[] indirectPrerequisites;

    /**
     * An array of sub-tasks for this task.
     */
    protected Task[] subTasks;

    /**
     * The type of the task.
     * <ul>
     *   <li>{@code true} indicates a primitive task.</li>
     *   <li>{@code false} indicates an composite task.</li>
     * </ul>
     */
    protected final boolean type;

    /**
     * Constructs a Task with the specified name, description, duration, direct prerequisites and automatically derives all the prerequisites.
     *
     * @param name                the name of the task
     * @param description         the description of the task
     * @param duration            the duration of the task
     * @param directPrerequisites the direct prerequisites of the task
     * @param subTasks            the subtasks of the composite task
     * @param type                the type of the task (true for primitive, false for composite)
     */
    public Task(String name, String description, double duration, Task[] directPrerequisites, Task[] subTasks, boolean type) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.directPrerequisites = directPrerequisites;
        this.indirectPrerequisites = deriveIndirectPrerequisites(directPrerequisites, subTasks);
        this.subTasks = subTasks;
        this.type = type; // true for primitive and false for composite
    }

    /**
     * Returns the name of the task.
     *
     * @return the name of the task
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the description of the task.
     *
     * @return the description of the task
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns the duration of the task.
     *
     * @return the duration of the task
     */
    public double getDuration() {
        return this.duration;
    }

    /**
     * Returns the direct prerequisites of the task.
     *
     * @return the direct prerequisites of the task
     */
    public Task[] getDirectPrerequisites() {
        return this.directPrerequisites;
    }

    /**
     * Returns the indirect prerequisites of the task.
     *
     * @return the indirect prerequisites of the task
     */
    public Task[] getIndirectPrerequisites() {
        return this.indirectPrerequisites;
    }

    /**
     * Returns the subtasks of the composite task.
     *
     * @return the subtasks of the composite task
     */
    public Task[] getSubTasks() {
        return this.subTasks;
    }

    /**
     * Returns the type of the task.
     *
     * @return true if the task is a primitive task, false if it is a composite task
     */
    public boolean getType() {
        return this.type;
    }

    /**
     * Sets the name of the task.
     *
     * @param name the new name of the task
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the description of the task.
     *
     * @param description the new description of the task
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the duration of the task.
     *
     * @param duration the new duration of the task
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * Sets the direct prerequisites of the task and derives the indirect prerequisites accordingly.
     *
     * @param directPrerequisites the new direct prerequisites of the task
     */
    public void setDirectPrerequisites(Task[] directPrerequisites) {
        this.directPrerequisites = directPrerequisites;
        this.indirectPrerequisites = deriveIndirectPrerequisites(directPrerequisites, this.subTasks);
    }

    /**
     * Checks if the specified name is a valid task name.
     * A valid task name cannot be null, must have a length of at most 8 characters,
     * and cannot start with a digit. It must consist only of letters and digits.
     *
     * @param name the name to be validated
     * @return true if the name is valid, false otherwise
     */
    public static boolean isValidName(String name) {
        if (name == null) {
            return false;
        }
        if (name.length() > 8) {
            return false;
        }
        if (Character.isDigit(name.charAt(0))) {
            return false;
        }
        for (char c : name.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the specified description is a valid task description.
     * A valid task description can only consist of letters, digits, and hyphens.
     *
     * @param description the description to be validated
     * @return true if the description is valid, false otherwise
     */
    public static boolean isValidDescription(String description) {
        for (char c : description.toCharArray()) {
            if(!Character.isLetterOrDigit(c) && c != '-') {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the specified duration is a valid task duration.
     * A valid task duration must be greater than 0.
     *
     * @param duration the duration to be validated
     * @return true if the duration is valid (positive), false otherwise
     */
    public static boolean isValidDuration(double duration) {
        return !(duration <= 0);
    }

    /**
     * Checks if the input of the duration is valid
     * The input should be numeric
     *
     * @param durationInput the user input of duration
     * @return true if the duration input is valid, false otherwise
     */
    public static boolean isValidDurationInput(String durationInput) {
        try {
            Double.parseDouble(durationInput);
            return true;
        } catch (NumberFormatException e){
            return false;
        }
    }

    /**
     * Checks if the prerequisite input is valid
     * The input should be comma-separated
     *
     * @param prerequisiteInput the user input of prerequisites
     * @return true if the input is comma-separated, false otherwise
     */
    public static boolean isValidPrerequisiteInput(String prerequisiteInput) {
        return prerequisiteInput.contains(",");
    }

    /**
     * Checks if the specified subtask input is valid.
     * This method delegates the validation to the isValidPrerequisiteInput method.
     *
     * @param subtaskInput the subtask input to be validated
     * @return true if the subtask input is valid, false otherwise
     */
    public static boolean isValidSubtaskInput(String subtaskInput) {
        return isValidPrerequisiteInput(subtaskInput);
    }

    /**
     * Retrieves an array of Task objects based on the specified names and taskRecorder.
     * This method retrieves the corresponding Task objects from the taskRecorder based on the given names.
     *
     * @param names        the names of the tasks to retrieve
     * @param taskRecorder the taskRecorder to retrieve the tasks from
     * @return an array of Task objects
     */
    public static Task[] getTaskList(String[] names, TaskRecorder taskRecorder) {
        Task[] taskList = new Task[names.length];
        for (int i = 0; i < names.length; ++i) {
            taskList[i] = taskRecorder.getTask(names[i]);
        }
        return taskList;
    }

    /**
     * Checks if the specified task list contains the specified task.
     *
     * @param taskList the task list to be checked
     * @param task     the task to be checked for existence
     * @return true if the task list contains the task, false otherwise
     */
    public static boolean contains(Task[] taskList, Task task) {
        for (Task t : taskList) {
            if (Objects.equals(t.getName(), task.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the specified task is present in the given taskList.
     *
     * @param taskList the list of tasks to search in
     * @param task     the task to search for
     * @return true if the task is present in the taskList, false otherwise
     */
    public static boolean contains(List<Task> taskList, Task task) {
        for (Task t :taskList) {
            if (Objects.equals(t.getName(), task.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Derives the indirect prerequisites based on the direct prerequisites and subtasks.
     *
     * @param directPrerequisites the direct prerequisites of the task
     * @param subTasks            the subtasks of the composite task
     * @return an array of indirect prerequisites
     */
    public static Task[] deriveIndirectPrerequisites(Task[] directPrerequisites, Task[] subTasks) {
        List<Task> indirectPrerequisites = new ArrayList<>();
        for (Task prerequisite : directPrerequisites) {
            indirectPrerequisites.add(prerequisite); // 将直接先决条件添加到间接先决条件列表中
            addIndirectPrerequisites(prerequisite, indirectPrerequisites, subTasks);
        }
        for (Task prerequisite : directPrerequisites) {
            indirectPrerequisites.remove(prerequisite);
        }
        return indirectPrerequisites.toArray(new Task[0]);
    }

    /**
     * Recursively adds the indirect prerequisites of a task to the indirectPrerequisites list.
     *
     * @param task                 the task to add indirect prerequisites for
     * @param indirectPrerequisites the list to store the indirect prerequisites
     * @param subTasks             the subtasks of the composite task
     */
    private static void addIndirectPrerequisites(Task task, List<Task> indirectPrerequisites, Task[] subTasks) {

        if (task == null) {
            return;
        }

        for (Task directPrerequisite : task.getDirectPrerequisites()) {
            if (!indirectPrerequisites.contains(directPrerequisite) && !contains(subTasks, directPrerequisite)) {
                indirectPrerequisites.add(directPrerequisite);
            }
            addIndirectPrerequisites(directPrerequisite, indirectPrerequisites, subTasks);
        }

    }

}

