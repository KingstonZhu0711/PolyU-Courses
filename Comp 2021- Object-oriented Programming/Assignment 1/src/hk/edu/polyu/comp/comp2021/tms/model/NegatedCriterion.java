package hk.edu.polyu.comp.comp2021.tms.model;

/**
 * This class represents a negated criterion.
 * Extends the CompositeCriterion class.
 *
 * @author Zhu Jin Shun
 * @version 1.0
 */
public class NegatedCriterion extends CompositeCriterion{

    /**
     * Constructs a NegatedCriterion object with the specified name and sub-criterion.
     *
     * @param name              the name of the negated criterion
     * @param firstSubCriterion the sub-criterion to be negated
     */
    public NegatedCriterion(String name, Criterion firstSubCriterion) {
        super(name, firstSubCriterion, null,null, "Negated");
    }

}