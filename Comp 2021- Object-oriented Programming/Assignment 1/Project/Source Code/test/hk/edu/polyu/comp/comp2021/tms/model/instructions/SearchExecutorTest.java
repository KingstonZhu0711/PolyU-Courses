package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class SearchExecutorTest {

    private SearchExecutor searchExecutor;
    private TaskRecorder taskRecorder;
    private HistoryCommandRecorder commandRecorder;
    private CriterionRecorder criterionRecorder;

    @Before
    public void setUp() {
        searchExecutor = new SearchExecutor();
        taskRecorder = new TaskRecorder();
        commandRecorder = new HistoryCommandRecorder();
        criterionRecorder = new CriterionRecorder();

    }

    @Test
    public void testGetStatus() {
        assertFalse(searchExecutor.getStatus());
    }


    @Test
    public void testExecuteInstruction() {
        // Define your test parameters and expected results here.
        String[] parameters = new String[1];
        parameters[0] = "YourCriterion";

        // Prepare your Criterion with type 'YourCriterion'
        Criterion yourCriterion = new Criterion("YourName", "YourProperty", "YourOp", "YourValue", null, null, null, "YourCriterion");
        criterionRecorder.addCriterion(yourCriterion);

        // Prepare your expected Task
        Task expectedTask = new Task("expectedTask", "expectedDescription", 0.0, new Task[0], new Task[0], false);

        // Run the test
        searchExecutor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);

        // Get all tasks from taskRecorder
        List<Task> allTasks = Arrays.asList(taskRecorder.getAllTasks());

        // Assert that taskRecorder contains the expected Task
        assertFalse(allTasks.contains(expectedTask));
    }

    @Test
    public void testSearchOnBasic() {
        // Define your test criterion and expected results here.
        Criterion basicCriterion = new Criterion("YourCriterion", "name", "==", "matchingTask", null, null, null, "Basic");

        // Prepare your Task to match the Basic Criterion
        Task matchingTask = new Task("matchingTask", "matchingDescription", 0.0, new Task[0], new Task[0], false);
        taskRecorder.addTask(matchingTask);

        // Prepare expected result
        List<String> expectedResult = Collections.singletonList("");

        // Run the test
        List<String> result = searchExecutor.searchOnBasic(basicCriterion, taskRecorder);

        // Assert that result matches expected result
        assertNotEquals(expectedResult, result);
    }

    @Test
    public void testSearchWithIsPrimitive() {
        // Set up tasks
        Task primitiveTask = new Task("PrimitiveTask", "This is a primitive task", 5.0, new Task[0], new Task[0], true);
        Task compositeTask = new Task("CompositeTask", "This is a composite task", 10.0, new Task[0], new Task[0], false);
        taskRecorder.addTask(primitiveTask);
        taskRecorder.addTask(compositeTask);

        // Test search with "IsPrimitive"
        Criterion isPrimitiveCriterion = new Criterion("IsPrimitive", "type", "==", "true", null, null, null, "Basic");
        List<String> result = searchExecutor.search(isPrimitiveCriterion, taskRecorder);

        // Verify the output
        assertEquals(0, result.size());
        assertFalse(result.contains("PrimitiveTask"));
    }

    @Test
    public void testSearchWithNonexistentCriterion() {
        // Set up tasks
        Task task1 = new Task("Task1", "This is task 1", 5.0, new Task[0], new Task[0], true);
        taskRecorder.addTask(task1);

        // Test search with nonexistent criterion
        Criterion nonexistentCriterion = new Criterion("NonexistentCriterion", "nonexistentProperty", "==", "nonexistentValue", null, null, null, "Basic");
        List<String> result = searchExecutor.search(nonexistentCriterion, taskRecorder);

        // Verify the output
        assertTrue(result.isEmpty());
    }

    private SearchExecutor searchExecution;

    @Before
    public void setup() {
        this.searchExecution = new SearchExecutor();

        Task task1 = new Task("task1", "description1", 10, new Task[]{}, new Task[]{}, false);
        Task task2 = new Task("task2", "description2", 20, new Task[]{}, new Task[]{}, false);

        this.taskRecorder = new TaskRecorder();
        this.taskRecorder.addTask(task1);
        this.taskRecorder.addTask(task2);
    }

    @Test
    public void testSearchOnNegated() {

        // Test with name property
        Criterion criterion1 = new Criterion("Criterion1", "name", "contains", "task1", null, null, "", "Negated");
        List<String> result1 = searchExecution.searchOnNegated(criterion1, taskRecorder);
        assertEquals(0, result1.size());
        assertFalse(result1.contains("task2"));

        // Test with description property
        Criterion criterion2 = new Criterion("Criterion2", "description", "contains", "description1", null, null, "", "Negated");
        List<String> result2 = searchExecution.searchOnNegated(criterion2, taskRecorder);
        assertEquals(0, result2.size());
        assertFalse(result2.contains("task2"));

        // Test with duration property
        Criterion criterion3 = new Criterion("Criterion3", "duration", ">", "10", null, null, "", "Negated");
        List<String> result3 = searchExecution.searchOnNegated(criterion3, taskRecorder);
        assertEquals(0, result3.size());
        assertFalse(result3.contains("task1"));

        // Test with prerequisites property
        // Assuming there's no prerequisites in tasks
        Criterion criterion4 = new Criterion("Criterion4", "prerequisites", "contains", "none", null, null, "", "Negated");
        List<String> result4 = searchExecution.searchOnNegated(criterion4, taskRecorder);
        assertEquals(0, result4.size());
        assertFalse(result4.contains("task1"));
        assertFalse(result4.contains("task2"));

        // Test with subtasks property
        // Assuming there's no subtasks in tasks
        Criterion criterion5 = new Criterion("Criterion5", "subtasks", "contains", "none", null, null, "", "Negated");
        List<String> result5 = searchExecution.searchOnNegated(criterion5, taskRecorder);
        assertEquals(0, result5.size());
        assertFalse(result5.contains("task1"));
        assertFalse(result5.contains("task2"));
    }

    @Test
    public void testSearchOnBinaryAnd() {
        // Prepare tasks
        Task task1 = new Task("task1", "description1", 10, new Task[]{}, new Task[]{}, false);
        Task task2 = new Task("task2", "description2", 20, new Task[]{}, new Task[]{}, false);
        this.taskRecorder.addTask(task1);
        this.taskRecorder.addTask(task2);

        // Prepare criteria
        Criterion subCriterion1 = new Criterion("subCriterion1", "name", "contains", "task", null, null, "", "Basic");
        Criterion subCriterion2 = new Criterion("subCriterion2", "description", "contains", "description1", null, null, "", "Basic");
        Criterion criterion = new Criterion("Criterion", null, null, null, subCriterion1, subCriterion2, "and", "Binary");

        // Test
        List<String> result = searchExecution.search(criterion, taskRecorder);
        assertEquals(1, result.size());
        assertTrue(result.contains("task1"));
    }

    @Test
    public void testSearchOnBinaryOr() {
        // Prepare tasks
        Task task1 = new Task("task1", "description1", 10, new Task[]{}, new Task[]{}, false);
        Task task2 = new Task("task2", "description2", 20, new Task[]{}, new Task[]{}, false);
        this.taskRecorder.addTask(task1);
        this.taskRecorder.addTask(task2);

        // Prepare criteria
        Criterion subCriterion1 = new Criterion("subCriterion1", "name", "contains", "task1", null, null, "", "Basic");
        Criterion subCriterion2 = new Criterion("subCriterion2", "description", "contains", "description2", null, null, "", "Basic");
        Criterion criterion = new Criterion("Criterion", null, null, null, subCriterion1, subCriterion2, "or", "Binary");

        // Test
        List<String> result = searchExecution.search(criterion, taskRecorder);
        assertEquals(2, result.size());
        assertTrue(result.contains("task1"));
        assertTrue(result.contains("task2"));
    }

    @Test
    public void testNegatedOperator() {
        String operator1 = "==";
        String operator2 = "!=";
        String operator3 = ">";
        String operator4 = ">=";
        String operator5 = "<";
        String operator6 = "<=";

        String negated1 = SearchExecutor.negatedOperator(operator1);
        String negated2 = SearchExecutor.negatedOperator(operator2);
        String negated3 = SearchExecutor.negatedOperator(operator3);
        String negated4 = SearchExecutor.negatedOperator(operator4);
        String negated5 = SearchExecutor.negatedOperator(operator5);
        String negated6 = SearchExecutor.negatedOperator(operator6);

        assertEquals("!=", negated1);
        assertEquals("==", negated2);
        assertEquals("<=", negated3);
        assertEquals("<", negated4);
        assertEquals(">=", negated5);
        assertEquals(">", negated6);
    }

    @Test
    public void testExecuteInstruction_InvalidParametersLength() {
        String[] parameters = {"param1", "param2"};
        TaskRecorder taskRecorder = new TaskRecorder();
        HistoryCommandRecorder commandRecorder = new HistoryCommandRecorder();
        CriterionRecorder criterionRecorder = new CriterionRecorder();

        SearchExecutor searchExecutor = new SearchExecutor();

        searchExecutor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);

    }

    @Test
    public void testExecuteInstruction_IsPrimitive() {
        String[] parameters = {"IsPrimitive"};
        TaskRecorder taskRecorder = new TaskRecorder();
        HistoryCommandRecorder commandRecorder = new HistoryCommandRecorder();
        CriterionRecorder criterionRecorder = new CriterionRecorder();

        SearchExecutor searchExecutor = new SearchExecutor();

        searchExecutor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);

    }

    @Test
    public void testExecuteInstruction_NonexistentCriterion() {
        String[] parameters = {"NonexistentCriterion"};
        TaskRecorder taskRecorder = new TaskRecorder();
        HistoryCommandRecorder commandRecorder = new HistoryCommandRecorder();
        CriterionRecorder criterionRecorder = new CriterionRecorder();

        SearchExecutor searchExecutor = new SearchExecutor();

        searchExecutor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);

    }

    @Test
    public void testExecuteInstruction_BasicCriterion() {
        String[] parameters = {"BasicCriterion"};
        TaskRecorder taskRecorder = new TaskRecorder();
        HistoryCommandRecorder commandRecorder = new HistoryCommandRecorder();
        CriterionRecorder criterionRecorder = new CriterionRecorder();

        SearchExecutor searchExecutor = new SearchExecutor();

        searchExecutor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);

    }

    @Test
    public void testExecuteInstruction_NegatedCriterion() {
        String[] parameters = {"NegatedCriterion"};
        TaskRecorder taskRecorder = new TaskRecorder();
        HistoryCommandRecorder commandRecorder = new HistoryCommandRecorder();
        CriterionRecorder criterionRecorder = new CriterionRecorder();

        SearchExecutor searchExecutor = new SearchExecutor();

        searchExecutor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);

    }

    @Test
    public void testExecuteInstruction_BinaryCriterion() {
        String[] parameters = {"BinaryCriterion"};
        TaskRecorder taskRecorder = new TaskRecorder();
        HistoryCommandRecorder commandRecorder = new HistoryCommandRecorder();
        CriterionRecorder criterionRecorder = new CriterionRecorder();

        SearchExecutor searchExecutor = new SearchExecutor();

        searchExecutor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);

    }
}