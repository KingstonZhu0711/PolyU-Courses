package hk.edu.polyu.comp.comp2021.tms.model.instructions;

import hk.edu.polyu.comp.comp2021.tms.model.*;

/**
 * Implementation of the InstructionExecutor interface for executing the "printAllCriteria" instruction.
 * This executor prints information about all the criteria stored in the criterion recorder.
 *
 * @author Zeng Tianyi
 *
 * @version 1.0
 */
public class PrintAllCriteriaExecutor implements InstructionExecutor {

    private final boolean status;

    /**
     * Constructs a PrintAllCriteriaExecutor object.
     * The initial status is set to false.
     */
    public PrintAllCriteriaExecutor() {
        this.status = false;
    }

    /**
     * Gets the status of the executor.
     *
     * @return the status of the executor
     */
    @Override
    public boolean getStatus() {
        return this.status;
    }

    @Override
    public void undoInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {}

    /**
     * Executes the "printAllCriteria" instruction with the provided parameters.
     *
     * @param parameters the parameters for the instruction
     * @param taskRecorder the task recorder instance
     * @param commandRecorder the history command recorder
     * @param criterionRecorder the criterion recorder
     */
    @Override
    public void executeInstruction(String[] parameters, TaskRecorder taskRecorder, HistoryCommandRecorder commandRecorder, CriterionRecorder criterionRecorder) {

        Criterion[] allCriteria = criterionRecorder.getAllCriteria();
        int size = allCriteria.length;

        for (int i = 0; i < size; i++){
            System.out.println("------" + "The information for criterion "+ allCriteria[i].getName() + "------");

            if (allCriteria[i].getType().equals("Basic")){
                System.out.println("Property name: "+allCriteria[i].getProperty());
                System.out.println("Op: "+allCriteria[i].getOp());
                System.out.println("Value: "+allCriteria[i].getValue().toString());
                System.out.println ("Type: "+allCriteria[i].getType());
                System.out.println();
            }

            if (allCriteria[i].getType().equals("Binary")){
                System.out.println("The property name of its firstSubCriterion: "+(allCriteria[i].getFirstSubCriterion().getProperty())+" "+
                        "The property for of secondSubCriterion: "+allCriteria[i].getSecondSubCriterion().getProperty());
                System.out.println("The op of its firstSubCriterion: "+allCriteria[i].getFirstSubCriterion().getOp()+" "+
                        "The op of its secondSubCriterion: "+allCriteria[i].getSecondSubCriterion().getOp());
                System.out.println("The value of its firstSubCriterion: "+allCriteria[i].getFirstSubCriterion().getValue()+" "+
                        "The value of its secondSubCriterion: "+allCriteria[i].getSecondSubCriterion().getValue());
                System.out.println("The logicOp is: "+allCriteria[i].getLogicOp());
                System.out.println("Type: Binary (Composite)");
                System.out.println();
            }

            if (allCriteria[i].getType().equals("Negated")){
                System.out.println("The property name: "+allCriteria[i].getFirstSubCriterion().getProperty());
                if(allCriteria[i].getFirstSubCriterion().getOp().equals("contains")){
                    System.out.println("The op of the negated criterion: not contains");
                }
                if(allCriteria[i].getFirstSubCriterion().getOp().equals(">")){
                    System.out.println("The op of the negated criterion: <=");
                }
                if(allCriteria[i].getFirstSubCriterion().getOp().equals("<")){
                    System.out.println("The op of the negated criterion: >=");
                }
                if(allCriteria[i].getFirstSubCriterion().getOp().equals(">=")){
                    System.out.println("The op of the negated criterion: <");
                }
                if(allCriteria[i].getFirstSubCriterion().getOp().equals("<=")){
                    System.out.println("The op of the negated criterion: >");
                }
                if(allCriteria[i].getFirstSubCriterion().getOp().equals("!=")){
                    System.out.println("The op of the negated criterion: ==");
                }
                if(allCriteria[i].getFirstSubCriterion().getOp().equals("==")){
                    System.out.println("The op of the negated criterion: !=");
                }
                System.out.println("The value of the negated criterion: "+allCriteria[i].getFirstSubCriterion().getValue());
                System.out.println("The logicOp of the negated criterion: negation");
                System.out.println("Type: Negated (Composite)");
                System.out.println();
            }
        }
    }

}

