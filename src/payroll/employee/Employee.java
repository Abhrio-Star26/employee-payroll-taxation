package payroll.employee;

import java.util.List;

/**
 * Abstract base class of the inheritance hierarchy.
 * Every employee tier must say how its earnings, deductions
 * and projected annual income are calculated.
 */
public abstract class Employee {

    private final String id;
    private final String name;
    private final String department;
    private final double baseAmount;   // monthly basic / daily rate / monthly stipend
    private final int daysWorked;      // days present in the month

    protected Employee(String id, String name, String department,
                       double baseAmount, int daysWorked) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.baseAmount = baseAmount;
        this.daysWorked = daysWorked;
    }

    // ---------- abstract methods (polymorphism) ----------

    public abstract EmployeeType getType();

    /** Earnings for this month (Basic, HRA, DA, ...). */
    public abstract List<PayComponent> calculateEarnings(int workingDays);

    /** Non-tax deductions for this month (PF, Professional Tax, ...). */
    public abstract List<PayComponent> calculateDeductions(int workingDays);

    /** Full-year income used to estimate income tax. */
    public abstract double projectedAnnualIncome(int workingDays);

    /** Only salaried employees get the standard deduction. */
    public abstract boolean isStandardDeductionEligible();

    // ---------- common helpers ----------

    /** Fraction of the month actually worked (for loss-of-pay). */
    protected double attendanceRatio(int workingDays) {
        if (workingDays <= 0) {
            return 0;
        }
        return Math.min(daysWorked, workingDays) / (double) workingDays;
    }

    public static double sum(List<PayComponent> components) {
        double total = 0;
        for (PayComponent c : components) {
            total += c.getAmount();
        }
        return total;
    }

    // ---------- getters ----------

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getBaseAmount() { return baseAmount; }
    public int getDaysWorked() { return daysWorked; }

    @Override
    public String toString() {
        return id + " - " + name + " (" + getType() + ", " + department + ")";
    }
}
