package hk.edu.polyu.comp.comp2021.tms.model;
/**
 * This class represents a binary criterion.
 * Extends the CompositeCriterion class.
 *
 * @author Zhu Jin Shun
 * @version 1.0
 */
public class BinaryCriterion extends CompositeCriterion{
    /**
     * Constructs a BinaryCriterion object with the specified name, sub-criteria, and logic operator.
     *
     * @param name              the name of the binary criterion
     * @param firstSubCriterion the first sub-criterion
     * @param secondSubCriterion the second sub-criterion
     * @param logicOp           the logic operator used to combine the sub-criteria
     */
    public BinaryCriterion(String name, Criterion firstSubCriterion, Criterion secondSubCriterion, String logicOp) {
        super(name, firstSubCriterion, secondSubCriterion, logicOp, "Binary");
    }
}
