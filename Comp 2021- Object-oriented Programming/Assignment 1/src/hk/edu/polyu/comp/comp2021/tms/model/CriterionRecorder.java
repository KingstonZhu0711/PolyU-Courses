package hk.edu.polyu.comp.comp2021.tms.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.LinkedHashMap;

/**
 * This class represents a recorder of criteria.
 * It stores every criterion that the user created.
 * It provides implementations of adding, deleting, retrieving,
 * and displaying criteria stored in the recorder.
 *
 * @author Zeng Tianyi
 * @version 2.0
 */
public class CriterionRecorder {
    private final Map<String, Criterion> criterionMap;

    /**
     * A constructor for the class CriterionRecorder utilizing HashMap
     */
    public CriterionRecorder(){
        criterionMap = new LinkedHashMap<>();
    }

    /**
     * A method to add a criterion to the criterion recorder
     * @param criterion the criterion to be added
     */
    public void addCriterion(Criterion criterion){
        criterionMap.put(criterion.getName(), criterion);
    }

    /**
     * A method to delete a criterion to the criterion recorder based on its name.
     * @param criterionName the name of the criterion to be deleted
     */
    public void deleteCriterion(String criterionName){
        criterionMap.remove(criterionName);
    }

    /**
     * A method to check whether a criterion is in the recorder
     * @param criterionName the name of the criterion to be checked
     * @return true if the criterion is in the recorder, false otherwise
     */
    public boolean isInRecorder(String criterionName){
        return criterionMap.containsKey(criterionName);
    }

    /**
     * A method to get all criteria in the recorder
     * @return an array containing elements of type Criterion
     */
    public Criterion[] getAllCriteria(){
        List<Criterion> allCriteria = new ArrayList<>(criterionMap.values());
        return allCriteria.toArray(new Criterion[0]);
    }

    /**
     * A method to get a criterion based on its name
     * The purpose of this method is to facilitate the implementation of showCriterion function
     * @param criterionName the name of the criterion to be retrieved
     * @return a Criterion object
     */
    public Criterion getCriterion(String criterionName) {
        return criterionMap.get(criterionName);
    }

    /**
     * A method to display a criterion's entire attributes (name, property, op, and value)
     * @param criterionName the name of the criterion to be displayed
     */
    public void showCriterion(String criterionName){
        Criterion targetCriterion = getCriterion(criterionName);
        System.out.println();
        System.out.println("--------Information for " + criterionName + "--------");
        System.out.println("Criterion Name: " + criterionName);
        System.out.println("Property: " + targetCriterion.getProperty());
        System.out.println("Op: " + targetCriterion.getOp());
        System.out.println("Value: " + targetCriterion.getValue());
    }

}
