package hk.edu.polyu.comp.comp2021.tms.model;

/**
 * A basic criterion used for task filtering.
 *
 * @author Zeng Tianyi
 * @version 1.0
 */
public class BasicCriterion extends Criterion{

    /**
     * Constructs a BasicCriterion with the specified parameters.
     *
     * @param name     The name of the criterion.
     * @param property The property to compare.
     * @param op       The comparison operator.
     * @param value    The value to compare with.
     */
    public BasicCriterion(String name, String property, String op, Object value){
        super(name, property, op, value, null, null, null, "Basic");
    }

}
