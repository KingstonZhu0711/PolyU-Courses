package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;
import hk.edu.polyu.comp.comp2021.tms.model.instructions.PrintAllCriteriaExecutor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

public class PrintAllCriteriaExecutorTest {

    private TaskRecorder taskRecorder;
    private HistoryCommandRecorder commandRecorder;
    private CriterionRecorder criterionRecorder;
    private Criterion basicCriterion;
    private Criterion binaryCriterion;
    private Criterion negatedCriterion;

    @Before
    public void setUp() {
        taskRecorder = new TaskRecorder();
        commandRecorder = new HistoryCommandRecorder();
        criterionRecorder = new CriterionRecorder();

        // Create Criteria
        basicCriterion = new BasicCriterion("Criterion1", "Property1", "contains", "Value1");
        binaryCriterion = new BinaryCriterion("Criterion2", basicCriterion, basicCriterion, "AND");
        negatedCriterion = new NegatedCriterion("Criterion3", basicCriterion);

        criterionRecorder.addCriterion(basicCriterion);
        criterionRecorder.addCriterion(binaryCriterion);
        criterionRecorder.addCriterion(negatedCriterion);
    }

    @Test
    public void testExecuteInstruction() {
        PrintAllCriteriaExecutor executor = new PrintAllCriteriaExecutor();

        String[] parameters = new String[]{};
        executor.executeInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
    }

    @Test
    public void testUndoInstruction() {
        PrintAllCriteriaExecutor executor = new PrintAllCriteriaExecutor();

        String[] parameters = new String[]{};
        executor.undoInstruction(parameters, taskRecorder, commandRecorder, criterionRecorder);
        assertFalse(executor.getStatus());
    }
}