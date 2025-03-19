package hk.edu.polyu.comp.comp2021.tms.model;

/**
 * This is a class that represents a criterion.
 * It implements features of a criterion as per the requirement 9.
 *
 * @author Zeng Tianyi
 * @version 3.0
 */
public class Criterion {
    private final String name;
    private final String property;
    private final String op;
    private final Object value;

    private final Criterion firstSubCriterion;

    private final Criterion secondSubCriterion;

    private final String logicOp;

    private final String type;

    /**
     * Constructs a Criterion object with the specified parameters.
     *
     * @param name              The name of the criterion.
     * @param property          The property associated with the criterion.
     * @param op                The operator to be applied in the criterion.
     * @param value             The value to be compared in the criterion.
     * @param firstSubCriterion The first sub-criterion.
     * @param secondSubCriterion The second sub-criterion.
     * @param logicOp           The logical operator to be applied between sub-criteria.
     * @param type              The type of the criterion.
     */
    public Criterion(String name, String property, String op, Object value, Criterion firstSubCriterion, Criterion secondSubCriterion, String logicOp, String type){
        this.name = name;
        this.property = property;
        this.op = op;
        this.value = value;
        this.firstSubCriterion = firstSubCriterion;
        this.secondSubCriterion= secondSubCriterion;
        this.logicOp = logicOp;
        this.type = type;
    }

    /**
     * Returns the name of a criterion
     * @return the name of a criterion
     */
    public String getName(){
        return this.name;
    }

    /**
     * Returns the property of a criterion
     * @return the property of a criterion
     */
    public String getProperty(){
        return this.property;
    }

    /**
     * Returns the op of a criterion
     * @return the op of a criterion
     */
    public String getOp(){
        return this.op;
    }

    /**
     * Returns the value of a criterion
     * @return the value of a criterion
     */
    public Object getValue(){
        return this.value;
    }

    /**
     * Retrieves the first sub-criterion of the composite criterion.
     *
     * @return The first sub-criterion.
     */
    public Criterion getFirstSubCriterion(){
        return this.firstSubCriterion;
    }

    /**
     * Retrieves the second sub-criterion of the composite criterion.
     *
     * @return The second sub-criterion.
     */
    public Criterion getSecondSubCriterion(){
        return this.secondSubCriterion;
    }

    /**
     * Retrieves the type of the criterion.
     *
     * @return The type of the criterion.
     */
    public String getType(){
        return this.type;
    }

    /**
     * Retrieves the type of the criterion.
     *
     * @return The logicOp of the criterion.
     */
    public String getLogicOp() {return this.logicOp;}

    /**
     * Checks the validity of the name of a criterion.
     * A valid task name cannot be null, must have a length of at most 8 characters,
     * and cannot start with a digit. It must consist only of letters and digits.
     *
     * @param name the name to be checked
     * @return true if the name is valid, false otherwise.
     */
    public static boolean isValidCriterionName(String name){
        if(name.length() >8){
            return false;
        }

        char firstChar = name.charAt(0);
        if (!isLetter(firstChar)) {
            return false;
        }

        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!isLetterOrDigit(c)) {
                return false;
            }
        }

        return true;
    }

    /**
     * A method to check whether a character is an English letter.
     * @param a the character to be checked
     * @return true if a(the parameter) is an English letter, false otherwise.
     */
    private static boolean isLetter(char a) {
        return (a >= 'A' && a <= 'Z') || (a >= 'a' && a <= 'z');
    }

    /**
     * A method to check whether a character is a digit.
     * @param a the character to be checked
     * @return true if a(the parameter) is a digit, false otherwise.
     */
    private static boolean isDigit(char a) {
        return a >= '0' && a <= '9';
    }

    /**
     * A method to check whether a character belongs to the union of Letter and Digit.
     * @param a the character to be checked
     * @return true if a(the parameter) belongs to the union of Letter and Digit, false otherwise.
     */
    private static boolean isLetterOrDigit(char a) {
        return isLetter(a) || isDigit(a);
    }



    /**
     *  A method to check the validity of the property
     *  @param property the property to be checked
     *  @return true if the property is valid, false otherwise.
     */
    public static boolean isValidCriterionProperty(String property){
        return property.equals("name") || property.equals("description") || property.equals("duration") || property.equals("prerequisites") || property.equals("subtasks");
        // Question: According to the description in [REQ9], the property should either be a name, description, duration, or prerequisites.
        // But in the requirement, it said when property is prerequisites or subtasks. This causes confusion.
    }

    /**
     * A method to check the validity of the op
     *
     * @param property the property as a pre-condition
     * @param op the op to be checked
     * @return 1) true if both the property is valid and op is valid. 2) false if the property is valid but op is not valid.
     * 3) false if the property is invalid (as property is the pre-condition of op).
     */
    public static boolean isValidCriterionOp(String property,String op){

        if(!isValidCriterionProperty(property)){
            System.out.println("Invalid property! The validity of op can't be determined as a consequence.");
            return false;
        }

        if(property.equals("name") || property.equals("description")){
            if(!op.equals("contains")){
                return false;
            }
        }

        if(property.equals("duration")){
            if(!op.equals(">") && !op.equals("<") && !op.equals(">=") && !op.equals("<=") && !op.equals("==") && !op.equals("!=") ){
                return false;
            }
        }

        if(property.equals("prerequisites") || property.equals("subtasks")){
            return op.equals("contains");
        }

        return true;

    }

    /**
     * A method to check the validity of the value of a criterion.
     * @param property the property of the criterion being checked
     * @param op the op of the criterion being checked
     * @param value the value of the criterion being checked
     * @return true if the value is valid, false otherwise
     */
    public static boolean isValidCriterionValue(String property, String op, Object value){

        if(!isValidCriterionProperty(property)){
            System.out.println("Invalid property! The validity of op and value can't be determined.");
            return false;
        }
        else{
            if(!isValidCriterionOp(property,op)){
                System.out.println("The property is valid, but op is invalid. The validity of value can't be determined as a consequence.");
                return false;
            }
            else{
                if(property.equals("name") || property.equals("description")){
                    if(!(value instanceof String) || !((String) value).startsWith("\"") && !((String) value).endsWith("\"")){
                        return false;
                    }
                }
                if (property.equals("duration")) {
                    try{
                        Double.parseDouble((String) value);
                        return true;
                    } catch(NumberFormatException e){
                        return false;
                    }
                }
                if(property.equals("prerequisites") || property.equals("subtasks")){
                    if(!(value instanceof String)){
                        return false;
                    }
                    else{
                        return ((String) value).contains(",");
                    }
                }
            }
        }
        return true;
    }

}
