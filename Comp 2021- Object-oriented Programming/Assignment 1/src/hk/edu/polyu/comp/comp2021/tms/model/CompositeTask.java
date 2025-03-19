package hk.edu.polyu.comp.comp2021.tms.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents a composite task, which is a task that consists of multiple subtasks.
 * It extends the Task class and provides methods to retrieve the subtasks and calculate the duration of the composite task.
 *
 * @author Wang Ruijie
 * @version 3.0
 */
public class CompositeTask extends Task {



    private static final Map<Task, Double> maxDurationMap = new HashMap<>();

    /**
     * Constructs a CompositeTask object with the specified name, description, and subtasks.
     * The duration and direct prerequisites of the composite task are calculated based on the subtasks.
     *
     * @param name        the name of the composite task
     * @param description the description of the composite task
     * @param subTasks    the subtasks of the composite task
     */
    public CompositeTask(String name, String description, Task[] subTasks) {
        super(name, description, deriveDuration(subTasks, maxDurationMap), deriveDirectPrerequisites(subTasks), subTasks, false);
    }

    /**
     * Sets the sub-tasks for the task.
     * Updates the subTasks array, directPrerequisites, indirectPrerequisites, and duration fields.
     *
     * @param subTasks The array of sub-tasks to be set.
     */
    public void setSubTasks(Task[] subTasks) {
        this.subTasks = subTasks;
        this.directPrerequisites = deriveDirectPrerequisites(subTasks);
        this.indirectPrerequisites = deriveIndirectPrerequisites(this.directPrerequisites, subTasks);
        this.duration = deriveDuration(subTasks, maxDurationMap);
    }

    /**
     * Calculates and returns the direct prerequisites of the composite task based on its subtasks.
     *
     * @param subTasks the subtasks of the composite task
     * @return the direct prerequisites of the composite task
     */
    public static Task[] deriveDirectPrerequisites(Task[] subTasks) {
        List<Task> directPrerequisites = new ArrayList<>();
        for (Task subTask : subTasks) {
            if (subTask.getType()) {
                for (Task directPrerequisite : subTask.getDirectPrerequisites()) {
                    if (!directPrerequisites.contains(directPrerequisite) && !contains(subTasks, directPrerequisite)) {
                        directPrerequisites.add(directPrerequisite);
                    }
                }
            } else {
                CompositeTask compositeSubTask = (CompositeTask) subTask;
                deriveDirectPrerequisites(compositeSubTask.getSubTasks());
            }
        }
        return directPrerequisites.toArray(new Task[0]);
    }

    /**
     * Calculates the total duration of the composite task and updates the maxDurationMap with the maximum durations of each subtask.
     *
     * @param subTasks       the subtasks of the composite task
     * @param maxDurationMap the map to store the maximum durations of each subtask
     */
    private static void calculateTotalDuration(Task[] subTasks, Map<Task, Double> maxDurationMap) {

        double totalDuration = 0;

        // 遍历子任务数组
        for (Task subTask : subTasks) {
            double subTaskDuration = 0;

            // 如果子任务是原始任务，直接使用其持续时间
            if (subTask.getType()) {
                subTaskDuration = subTask.getDuration();
            }
            // 如果子任务是复合任务，递归计算其持续时间
            else  {
                CompositeTask compositeSubTask = (CompositeTask) subTask;
                calculateTotalDuration(compositeSubTask.getSubTasks(), maxDurationMap);
            }

            // 更新子任务的最大持续时间
            maxDurationMap.put(subTask, subTaskDuration);

            // 更新总持续时间为子任务中的最大持续时间
            totalDuration = Math.max(totalDuration, subTaskDuration);
        }

        for (Task subTask : subTasks) {
            calculateTotalDurationRecursion(subTask, maxDurationMap);
        }
    }

    /**
     * Recursively calculates the total duration of the composite task and updates the maxDurationMap with the maximum durations of each subtask.
     *
     * @param subTask        the current subtask being processed
     * @param maxDurationMap the map to store the maximum durations of each subtask
     */
    private static void calculateTotalDurationRecursion(Task subTask, Map<Task, Double> maxDurationMap) {

        boolean detectedFlag = false;
        List<Task> detectedPrerequisites = new ArrayList<>();
        for (Task subSubTask : subTask.getDirectPrerequisites()) {
            if (maxDurationMap.containsKey(subSubTask)) {
                detectedFlag = true;
                detectedPrerequisites.add(subSubTask);
            }
        }
        if (detectedFlag) {
            for (Task detectedPrerequisite : detectedPrerequisites) {
                maxDurationMap.put(subTask, maxDurationMap.get(subTask) + maxDurationMap.get(detectedPrerequisite));
                calculateTotalDurationRecursion(detectedPrerequisite, maxDurationMap);
            }
        }
    }

    /**
     * Calculates the duration of the composite task based on its subtasks.
     *
     * @param subTasks       the subtasks of the composite task
     * @param maxDurationMap the map to store the maximum durations of each subtask
     * @return the duration of the composite task
     */
    private static double deriveDuration(Task[] subTasks, Map<Task, Double> maxDurationMap) {
        calculateTotalDuration(subTasks, maxDurationMap);
        Map.Entry<Task, Double> maxEntry = null;
        for (Map.Entry<Task, Double> entry : maxDurationMap.entrySet()) {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) {
                maxEntry = entry;
            }
        }
        assert maxEntry != null;
        return maxEntry.getValue();
    }
}
