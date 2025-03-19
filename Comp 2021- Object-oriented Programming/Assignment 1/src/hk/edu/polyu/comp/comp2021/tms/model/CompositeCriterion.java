package hk.edu.polyu.comp.comp2021.tms.model;

/**
 * A composite criterion used for combining sub-criteria with logical operators.
 *
 * @author Zeng Tianyi
 * @version 1.0
 */
public class CompositeCriterion extends Criterion{

    /**
     * Constructs a CompositeCriterion with the specified parameters.
     *
     * @param name              The name of the criterion.
     * @param firstSubCriterion The first sub-criterion.
     * @param secondSubCriterion The second sub-criterion.
     * @param logicOp           The logical operator to combine the sub-criteria.
     * @param type              The type of the criterion.
     */
    public CompositeCriterion(String name, Criterion firstSubCriterion, Criterion secondSubCriterion, String logicOp, String type){
        super(name, null, null, null, firstSubCriterion, secondSubCriterion, logicOp, type);
    }

}
