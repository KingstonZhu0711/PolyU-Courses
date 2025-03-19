package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;

import java.util.Objects;

import static hk.edu.polyu.comp.comp2021.tms.model.Task.*;

/**
 * This class implements the InstructionExecutor interface
 * and is responsible for executing instructions to change properties of a task.
 *
 * @author Wang Ruijie
 * @author Zhu Jin Shun
 *
 * @version 3.0
 */
public class ChangeTaskExecutor implements InstructionExecutor {

    private boolean status;

    private String changedProperty;

    private Object changedContent;

    /**
     * Initializes a new instance of the ChangeTaskExecutor class.
     */
    public ChangeTaskExecutor() {
        this.status = false;
    }

    /**
     * Gets the changed property of the task.
     *
     * @return The changed property of the task.
     */
    public String getChangedProperty() {
        return this.changedProperty;
    }

    /**
     * Gets the content of the changed property.
     *
     * @return The content of the changed property.
     */
    public Object getChangedContent() {
        return  this.changedContent;
    }

    /**
     * Retrieve the status of the executor
     *
     * @return true if the executor is successfully executed, false otherwise.
     */
    @Override
    public boolean getStatus() {
        return this.status;
    }

    /**
     * Executes the instruction to change properties of a task.
     *
     * @param parameters        The parameters of the instruction.
     * @param taskRecorder      The task recorder to manage tasks.
     * @param commandRecorder   The command recorder to record history commands.
     * @param criterionRecorder The criterion recorder to manage criteria.
     */
    @Override
    public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

        if (!(parameters.length == 3)) {
            return;
        }

        String taskName = parameters[0];
        String property = parameters[1];
        String newValue = parameters[2];

        if (!taskRecorder.existsTask(taskName)) {
            return;
            //
        }

        Task changingTask = taskRecorder.getTask(taskName);

        boolean isValidProperty = (changingTask instanceof PrimitiveTask
                ? Objects.equals(property, "name") || Objects.equals(property, "description") || Objects.equals(property, "duration") || Objects.equals(property, "prerequisites")
                : Objects.equals(property, "name") || Objects.equals(property, "description") || Objects.equals(property, "prerequisites") || Objects.equals(property, "subtasks"));

        if (!isValidProperty) {
            System.out.println("Invalid property. Please provide a valid property (name, description, or subtasks).");
            return;
            /**/
        }

        switch (property) {

            case "name":
                // Check if newValue is valid for the name property
                if (isValidName(newValue) && !taskRecorder.existsTask(newValue)) {
                    String oldName = changingTask.getName();
                    this.changedProperty = "name";
                    this.changedContent = oldName;
                    changingTask.setName(newValue);
                    taskRecorder.updateTaskName(oldName);
                    // Update all
                    for (Task task : taskRecorder.getAllTasks()) {
                        task.setDirectPrerequisites(task.getDirectPrerequisites());
                        if (task instanceof CompositeTask) {
                            ((CompositeTask) task).setSubTasks(task.getSubTasks());
                        }
                    }
                    this.status = true;
                } else {
                    System.out.println("Invalid name or has repeated name. Please provide a valid name.");
                }
                break;

            case "description":
                // Check if newValue is valid for the description property
                if (isValidDescription(newValue)) {
                    this.changedProperty = "description";
                    this.changedContent = changingTask.getDescription();
                    changingTask.setDescription(newValue);
                    this.status = true;
                } else {
                    System.out.println("Invalid description, please provide a valid description");
                }
                break;

            case "duration":
                // Check if newValue is valid for the name property
                double newDuration;

                if (isValidDurationInput(newValue)) {
                    newDuration = Double.parseDouble(newValue);
                    if (isValidDuration(newDuration)) {
                        this.changedProperty = "duration";
                        this.changedContent = "" + changingTask.getDuration();
                        changingTask.setDuration(newDuration);
                        // Update all composite tasks
                        for (Task task : taskRecorder.getAllTasks()) {
                            if (task instanceof CompositeTask) {
                                ((CompositeTask) task).setSubTasks(task.getSubTasks());
                            }
                        }
                        this.status = true;
                    } else {
                        System.out.println("Duration out of range");
                    }
                } else {
                    System.out.println("Invalid duration. Please provide a valid duration");
                }
                break;

            case "prerequisites":
                // Check if newValue is valid for the description property
                if (isValidPrerequisiteInput(newValue)) {
                    String[] prerequisiteNames = newValue.split(",");
                    if (TaskRecorder.allExist(prerequisiteNames, taskRecorder) || Objects.equals(prerequisiteNames[0], "")) {
                        Task[] prerequisites = Task.getTaskList(prerequisiteNames, taskRecorder);
                        this.changedProperty = "prerequisites";
                        this.changedContent = changingTask.getDirectPrerequisites();
                        changingTask.setDirectPrerequisites(prerequisites);
                        // Update all
                        for (Task task : taskRecorder.getAllTasks()) {
                            task.setDirectPrerequisites(task.getDirectPrerequisites());
                            if (task instanceof CompositeTask) {
                                ((CompositeTask) task).setSubTasks(task.getSubTasks());
                            }
                        }
                        this.status = true;
                    } else {
                        System.out.println("Invalid prerequisites. Please provide a valid prerequisites");
                    }
                } else {
                    System.out.println("Invalid prerequisite input");
                }
                break;

            case "subtasks":
                // Check if newValue is a valid subtask
                if (isValidSubtaskInput(newValue)) {
                    String[] subtasksNames = newValue.split(",");
                    if (TaskRecorder.allExist(subtasksNames, taskRecorder)) {
                        Task[] newSubtasks = Task.getTaskList(subtasksNames, taskRecorder);
                        this.changedProperty = "subtasks";
                        this.changedContent = changingTask.getSubTasks();
                        assert changingTask instanceof CompositeTask;
                        ((CompositeTask) changingTask).setSubTasks(newSubtasks);
                        // Update all
                        for (Task task : taskRecorder.getAllTasks()) {
                            task.setDirectPrerequisites(task.getDirectPrerequisites());
                        }
                        this.status = true;
                    } else {
                        System.out.println("Invalid subtasks. Please provide a valid subtasks");
                    }
                } else {
                    System.out.println("Invalid subtask input");
                }
                break;

            default:
                System.out.println("Invalid property. Please provide a valid property (name, description, or subtasks)");

        }
    }

    /**
     * Undoes the instruction to change properties of a task.
     *
     * @param parameters        The parameters of the instruction.
     * @param taskRecorder      The task recorder to manage tasks.
     * @param commandRecorder   The command recorder to record history commands.
     * @param criterionRecorder The criterion recorder to manage criteria.
     */
    @Override
    public void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

        String[] newParameters = new String[3];
        newParameters[1] = getChangedProperty();

        switch (getChangedProperty()) {

            case "name":
                newParameters[0] = parameters[2];
                newParameters[2] = parameters[0];
                break;

            case "description": case "duration":
                newParameters[0] = parameters[0];
                newParameters[2] = (String) getChangedContent();
                break;

            case "prerequisites": case "subtasks":
                newParameters[0] = parameters[0];
                StringBuilder prerequisiteNameString = new StringBuilder();
                prerequisiteNameString.append(",");
                for (Task prerequisite : (Task[]) getChangedContent()) {
                    prerequisiteNameString.append(prerequisite.getName()).append(",");
                }
                newParameters[2] = prerequisiteNameString.toString();
                break;
        }

        executeInstruction(newParameters, taskRecorder, commandRecorder, criterionRecorder);

    }

}