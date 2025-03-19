package hk.edu.polyu.comp.comp2021.tms.model;

import hk.edu.polyu.comp.comp2021.tms.model.instructions.*;

import java.util.HashMap;
import java.util.Map;

/**
 * This enum represents the available instructions in the task management system.
 * Each instruction is associated with an executor that handles the execution of the instruction.
 * It also provides methods to retrieve the instruction name, executor, and execute an instruction.
 *
 * @author Wang Ruijie
 * @version 2.0
 */
public enum InstructionEnumeration {

    /**
     * Represents the instruction to create a primitive task.
     */
    CreatePrimitiveTask("CreatePrimitiveTask", new CreatePrimitiveTaskExecutor()),

    /**
     * Represents the instruction to create a composite task.
     */
    CreateCompositeTask("CreateCompositeTask", new CreateCompositeTaskExecutor()),

    /**
     * Represents the instruction to delete a task.
     * Implemented by Wang Ruijie.
     */
    DeleteTask("DeleteTask", new DeleteTaskExecutor()),

    /**
     * Represents the instruction to change a task.
     */
    ChangeTask("ChangeTask", new ChangeTaskExecutor()),

    /**
     * Represents the instruction to print a task.
     */
    PrintTask("PrintTask", new PrintTaskExecutor()),

    /**
     * Represents the instruction to print all tasks.
     */
    PrintAllTasks("PrintAllTasks", new PrintAllTasksExecutor()),

    /**
     * Represents the instruction to report the duration of a task.
     */
    ReportDuration("ReportDuration", new ReportDurationExecutor()),

    /**
     * Represents the instruction to report the earliest finish time of a task.
     */
    ReportEarliestFinishTime("ReportEarliestFinishTime", new ReportEarliestFinishTimeExecutor()),

    /**
     * Represents the instruction to define a basic criterion.
     */
    DefineBasicCriterion("DefineBasicCriterion", new DefineBasicCriterionExecutor()),

    /**
     * Represents the instruction to define a negated criterion.
     */
    DefineNegatedCriterion("DefineNegatedCriterion", new DefineNegatedCriterionExecutor()),

    /**
     * Represents the instruction to define a binary criterion.
     */
    DefineBinaryCriterion("DefineBinaryCriterion", new DefineBinaryCriterionExecutor()),

    /**
     * Represents the instruction to print all criteria.
     */
    PrintAllCriteria("PrintAllCriteria", new PrintAllCriteriaExecutor()),

    /**
     * Represents the instruction to search for tasks.
     */
    Search("Search", new SearchExecutor()),

    /**
     * Represents the instruction to store data.
     */
    Store("Store", new StoreExecutor()),

    /**
     * Represents the instruction to load data.
     */
    Load("Load", new LoadExecutor()),

    /**
     * Represents the instruction to quit the program.
     */
    Quit("Quit", new QuitExecutor()),

    /**
     * Represents the instruction to undo the previous action.
     */
    Undo("Undo", new UndoExecutor()),

    /**
     * Represents the instruction to redo the previously undone action.
     */
    Redo("Redo", new RedoExecutor());

    /**
     * The name of the instruction
     */
    private final String instructionName;

    /**
     * The executor of the instruction
     */
    private final InstructionExecutor executor;

    /**
     * Constructs an InstructionEnumeration with the specified instruction name and executor.
     *
     * @param instructionName the name of the instruction
     * @param executor        the executor that handles the execution of the instruction
     */
    InstructionEnumeration(String instructionName, InstructionExecutor executor) {
        this.instructionName = instructionName;
        this.executor = executor;
    }

    /**
     * Returns the name of the instruction.
     *
     * @return the name of the instruction
     */
    public String getInstructionName() {
        return this.instructionName;
    }

    /**
     * Returns the corresponding executor.
     *
     * @return the executor of the instruction
     */
    public InstructionExecutor getExecutor() {
        return this.executor;
    }

    /**
     * Returns the status of corresponding executor.
     *
     * @return the status of the executor of the instruction
     */
    public boolean getExecutorStatus() {
        return getExecutor().getStatus();
    }

    /**
     * Executes the instruction with the specified parameters using the provided task recorder.
     *
     * @param parameters     The parameters for executing the instruction.
     * @param taskRecorder   The task recorder used to record and manage tasks.
     * @param commandRecorder The command recorder used to record executed commands.
     * @param criterionRecorder The criterion recorder used to record and manage criteria.
     */
    public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
    }


    private static final Map<String, InstructionEnumeration> instructionMap = new HashMap<>();

    static {
        for (InstructionEnumeration instruction : InstructionEnumeration.values()) {
            instructionMap.put(instruction.getInstructionName(), instruction);
        }
    }

    /**
     * Returns the map of instruction names to InstructionEnumeration objects.
     *
     * @return the instruction map
     */
    public static Map<String, InstructionEnumeration> getInstructionMap() {
        return instructionMap;
    }

}
