package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This is a class that implements [REQ13].
 * It lists all tasks that satisfy the criterion provided by user's input (the name of a criterion).
 *
 * @author Zeng Tianyi
 * @author Zhu Jin Shun
 * @author Wang Ruijie
 *
 * @version 5.0
 */
public class SearchExecutor implements InstructionExecutor {

    private final boolean status;

    /**
     * Construct the search executor
     * Set the status to be false
     */
    public SearchExecutor() {
        this.status = false;
    }

    @Override
    public boolean getStatus() {
        return this.status;
    }

    @Override
    public void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {}

    @Override
    public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

        if (parameters.length != 1) {
            System.out.println("Invalid input parameters");
            return;
        }

        if (Objects.equals(parameters[0], "IsPrimitive")) {
            List<String> primitiveTaskNameList = new ArrayList<>();
            List<String> compositeTaskNameList = new ArrayList<>();
            Task[] allTasks = taskRecorder.getAllTasks();
            for (Task task : allTasks) {
                if (task.getType()) {
                    primitiveTaskNameList.add(task.getName());
                } else {
                    compositeTaskNameList.add(task.getName());
                }
            }
            System.out.println("Primitive tasks: " + primitiveTaskNameList);
            System.out.println("Not primitive tasks: " + compositeTaskNameList);
            return;
        }

        if (!criterionRecorder.isInRecorder(parameters[0])) {
            System.out.println("The input criterion does not exist");
            return;
        }

        Criterion targetCriterion = criterionRecorder.getCriterion(parameters[0]);
        String type = targetCriterion.getType();
        List<String> resultList = new ArrayList<>();

        System.out.println("The corresponding task(s): ");
        switch (type) {
            case "Basic":
                resultList = searchOnBasic(targetCriterion, taskRecorder);
                break;
            case "Negated":
                Criterion matchingCriterion = targetCriterion.getFirstSubCriterion();
                resultList = searchOnNegated(matchingCriterion, taskRecorder);
                break;
            case "Binary":
                resultList = search(targetCriterion, taskRecorder);
                break;
            default:
                System.out.println("Null");
        }
        System.out.println(resultList);
    }

    /**
     * Searches for tasks based on a given basic criterion.
     *
     * @param basicCriterion The criterion used for searching.
     * @param taskRecorder   The task recorder containing all tasks.
     * @return A list of task names that match the search criteria.
     */
    public List<String> searchOnBasic(Criterion basicCriterion, TaskRecorder taskRecorder) {
        List<String> taskNameList = new ArrayList<>();
        String targetProperty = basicCriterion.getProperty();
        String targetOp = basicCriterion.getOp();
        Object targetValue = basicCriterion.getValue();
        Task[] allTasks = taskRecorder.getAllTasks();
        if (targetProperty.equals("name")) {
            if (targetOp.equals("contains")) {
                String nameResult = targetValue.toString();
                String result = nameResult.replace("\"", "");
                for (Task allTask : allTasks) {
                    if (allTask.getName().contains(result)) {
                        taskNameList.add(allTask.getName());
                    }
                }
            }
        }
        if (targetProperty.equals("description")) {
            if (targetOp.equals("contains")) {
                String descriptionResult = targetValue.toString();
                String result = descriptionResult.replace("\"", "");
                for (Task allTask : allTasks) {
                    if (allTask.getDescription().contains(result)) {
                        taskNameList.add(allTask.getName());
                    }
                }
            }
        }
        if (targetProperty.equals("duration")) {
            double doubleValue = Double.parseDouble((String) targetValue);
            for (Task allTask : allTasks) {
                if (targetOp.equals(">")) {
                    if (allTask.getDuration() > doubleValue) {
                        taskNameList.add(allTask.getName());
                    }
                }
                if (targetOp.equals("<")) {
                    if (allTask.getDuration() < doubleValue) {
                        taskNameList.add(allTask.getName());
                    }
                }
                if (targetOp.equals(">=")) {
                    if (allTask.getDuration() >= doubleValue) {
                        taskNameList.add(allTask.getName());
                    }
                }
                if (targetOp.equals("<=")) {
                    if (allTask.getDuration() <= doubleValue) {
                        taskNameList.add(allTask.getName());
                    }
                }
                if (targetOp.equals("!=")) {
                    if (allTask.getDuration() != doubleValue) {
                        taskNameList.add(allTask.getName());
                    }
                }
                if (targetOp.equals("==")) {
                    if (allTask.getDuration() == doubleValue) {
                        taskNameList.add(allTask.getName());
                    }
                }
            }
        }
        if (targetProperty.equals("prerequisites")) {
            if (targetOp.equals("contains")) {
                String prerequisitesResult = targetValue.toString();
                String result = prerequisitesResult.replace(",", "");
                for (Task allTask : allTasks) {
                    for (Task targetTask : allTask.getDirectPrerequisites()) {
                        if ((targetTask.getName()).contains(result)) {
                            taskNameList.add(allTask.getName());
                        }
                    }
                }
            }
        }
        if (targetProperty.equals("subtasks")) {
            if (targetOp.equals("contains")) {
                String subtasksResult = targetValue.toString();
                String result = subtasksResult.replace(",", "");
                for (Task allTask : allTasks) {
                    for (Task targetTask : allTask.getSubTasks()) {
                        if ((targetTask.getName()).contains(result)) {
                            taskNameList.add(allTask.getName());
                        }
                    }
                }
            }
        }
        return taskNameList;
    }

    /**
     * Searches for tasks based on a given negated criterion.
     *
     * @param negatedCriterion The criterion used for searching.
     * @param taskRecorder     The task recorder containing all tasks.
     * @return A list of task names that match the search criteria.
     */
    public List<String> searchOnNegated(Criterion negatedCriterion, TaskRecorder taskRecorder) {

        List<String> taskNameList = new ArrayList<>();

        Task[] allTasks = taskRecorder.getAllTasks();
        String negatedTargetProperty = negatedCriterion.getProperty();
        String negatedTargetOp = negatedCriterion.getOp();
        Object negatedTargetValue = negatedCriterion.getValue();

        if (negatedTargetProperty.equals("name")) {
            if (negatedTargetOp.equals("contains")) {
                String nameResult = negatedTargetValue.toString();
                String result = nameResult.replace("\"", "");
                for (Task allTask : allTasks) {
                    if (!allTask.getName().contains(result)) {
                        taskNameList.add(allTask.getName());
                    }
                }
            }
        }
        if (negatedTargetProperty.equals("description")) {
            if (negatedTargetOp.equals("contains")) {
                String descriptionResult = negatedTargetValue.toString();
                String result = descriptionResult.replace("\"", "");
                for (Task allTask : allTasks) {
                    if (!allTask.getDescription().contains(result)) {
                        taskNameList.add(allTask.getName());
                    }
                }
            }
        }
        if (negatedTargetProperty.equals("duration")) {
            double doubleValue = Double.parseDouble((String) negatedTargetValue);
            negatedTargetOp = negatedOperator(negatedTargetOp);
            for (Task allTask : allTasks) {
                if (negatedTargetOp.equals(">")) {
                    if (allTask.getDuration() > doubleValue) {
                        taskNameList.add(allTask.getName());
                    }
                }
                if (negatedTargetOp.equals("<")) {
                    if (allTask.getDuration() < doubleValue) {
                        taskNameList.add(allTask.getName());
                    }
                }
                if (negatedTargetOp.equals(">=")) {
                    if (allTask.getDuration() >= doubleValue) {
                        taskNameList.add(allTask.getName());
                    }
                }
                if (negatedTargetOp.equals("<=")) {
                    if (allTask.getDuration() <= doubleValue) {
                        taskNameList.add(allTask.getName());
                    }
                }
                if (negatedTargetOp.equals("!=")) {
                    if (allTask.getDuration() != doubleValue) {
                        taskNameList.add(allTask.getName());
                    }
                }
                if (negatedTargetOp.equals("==")) {
                    if (allTask.getDuration() == doubleValue) {
                        taskNameList.add(allTask.getName());
                    }
                }
            }
        }
        if (negatedTargetProperty.equals("prerequisites")) {
            if (negatedTargetOp.equals("contains")) {
                String prerequisitesResult = negatedTargetValue.toString();
                List<String> allTaskNameList = getStringList(prerequisitesResult, allTasks);
                taskNameList.addAll(allTaskNameList);
            }
        }
        if (negatedTargetProperty.equals("subtasks")) {
            String subtasksResult = negatedTargetValue.toString();
            List<String> allTaskNameList = getStrings(subtasksResult, allTasks);
            taskNameList.addAll(allTaskNameList);
        }
        return taskNameList;
    }

    /**
     * Retrieves a list of task names that are not included in the prerequisites result.
     *
     * @param prerequisitesResult The prerequisites result string.
     * @param allTasks            An array of all tasks.
     * @return A list of task names that are not included in the prerequisites result.
     */
    private static List<String> getStringList(String prerequisitesResult, Task[] allTasks) {
        String result = prerequisitesResult.replace(",", "");
        List<String> notIncludedTaskNameList = new ArrayList<>();
        List<String> allTaskNameList = new ArrayList<>();
        for (Task allTask : allTasks) {
            allTaskNameList.add(allTask.getName());
            for (Task targetTask : allTask.getDirectPrerequisites()) {
                if ((targetTask.getName()).contains(result)) {
                    notIncludedTaskNameList.add(allTask.getName());
                }
            }
        }
        allTaskNameList.removeAll(notIncludedTaskNameList);
        return allTaskNameList;
    }

    /**
     * Get the string list of the combination of subtasks
     * @param subtasksResult  The subtasks
     * @param allTasks        All existing tasks
     * @return a list of string of subtasks' names
     */
    private static List<String> getStrings(String subtasksResult, Task[] allTasks) {
        String result = subtasksResult.replace(",", "");
        List<String> notIncludedTaskNameList = new ArrayList<>();
        List<String> allTaskNameList = new ArrayList<>();
        for (Task allTask : allTasks) {
            allTaskNameList.add(allTask.getName());
            for (Task targetTask : allTask.getSubTasks()) {
                if ((targetTask.getName()).contains(result)) {
                    notIncludedTaskNameList.add(allTask.getName());
                }
            }
        }
        allTaskNameList.removeAll(notIncludedTaskNameList);
        return allTaskNameList;
    }

    /**
     * Returns the negated form of the given operator.
     *
     * @param operator The operator to be negated.
     * @return The negated form of the operator.
     */
    static String negatedOperator(String operator) {
        switch (operator) {
            case "==":
                operator = "!=";
                break;
            case "!=":
                operator = "==";
                break;
            case ">":
                operator = "<=";
                break;
            case ">=":
                operator = "<";
                break;
            case "<":
                operator = ">=";
                break;
            case "<=":
                operator = ">";
                break;
            default:
                break;
        }
        return operator;
    }

    /**
     * Searches for tasks based on the given criterion.
     *
     * @param criterion     The criterion used for searching.
     * @param taskRecorder  The task recorder containing all tasks.
     * @return A list of task names that match the search criteria.
     */
    public List<String> search(Criterion criterion, TaskRecorder taskRecorder) {

        List<String> resultTaskNameList = new ArrayList<>();

        if (Objects.equals(criterion.getType(), "Basic")) {
            resultTaskNameList = searchOnBasic(criterion, taskRecorder);
        }

        if (Objects.equals(criterion.getType(), "Negated")) {
            resultTaskNameList = searchOnNegated(criterion, taskRecorder);
        }

        if (Objects.equals(criterion.getType(), "Binary")) {

            List<String> firstList = new ArrayList<>();
            List<String> secondList = new ArrayList<>();


            if (Objects.equals(criterion.getFirstSubCriterion().getType(), "Basic")) {
                firstList.addAll(searchOnBasic(criterion.getFirstSubCriterion(), taskRecorder));
            }
            if (Objects.equals(criterion.getFirstSubCriterion().getType(), "Negated")) {
                firstList.addAll(searchOnNegated(criterion.getFirstSubCriterion(), taskRecorder));
            }
            if (Objects.equals(criterion.getFirstSubCriterion().getType(), "Binary")) {
                firstList.addAll(search(criterion.getFirstSubCriterion(), taskRecorder));
            }

            if (Objects.equals(criterion.getSecondSubCriterion().getType(), "Basic")) {
                secondList.addAll(searchOnBasic(criterion.getSecondSubCriterion(), taskRecorder));
            }
            if (Objects.equals(criterion.getSecondSubCriterion().getType(), "Negated")) {
                secondList.addAll(searchOnNegated(criterion.getSecondSubCriterion(), taskRecorder));
            }
            if (Objects.equals(criterion.getSecondSubCriterion().getType(), "Binary")) {
                secondList.addAll(search(criterion.getSecondSubCriterion(), taskRecorder));
            }


            if (Objects.equals(criterion.getLogicOp(), "and")) {
                List<String> intersection = firstList.stream()
                        .filter(secondList::contains)
                        .collect(Collectors.toList());

                resultTaskNameList.addAll(intersection);
            }

            if (Objects.equals(criterion.getLogicOp(), "or")) {
                resultTaskNameList.addAll(firstList);
                resultTaskNameList.removeAll(secondList);
                resultTaskNameList.addAll(secondList);
            }


        }

        return resultTaskNameList;
    }

}


