package hk.edu.polyu.comp.comp2021.assignment2.employee;

/**
 * A manager in a company.
 */
public class Manager extends Employee{
    private double bonusRate;

    /**
     * Initialize a manager object.
     */
    public Manager(String name, SalaryLevel level, double bonusRate){
        // Add missing code here.
        super(name, level);
        this.bonusRate = bonusRate;
    }

    public double getBonusRate(){
        return bonusRate;
    }

    public void setBonusRate(double bonusRate){
        this.bonusRate = bonusRate;
    }

    // Override method Employee.salary to calculate the salary of a manager.
    // The salary of a manager is computed as the multiplication
    // of his/her regular salary as an employee and his/her bonusRate plus 1.
    public double salary(){
        // Add missing code here.
        double regularSalary;
        regularSalary= super.salary();
        double salary;
        salary=regularSalary * (bonusRate + 1);
        return salary;
    }

}
