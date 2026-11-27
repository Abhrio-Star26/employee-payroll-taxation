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
    private final int leaveDays;       // days absent in the month (from CSV)

    protected Employee(String id, String name, String department,
                       double baseAmount, int leaveDays) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.baseAmount = baseAmount;
        this.leaveDays = leaveDays;
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

    /** Leave days, never more than the working days of the month. */
    public int getLeaveDays(int workingDays) {
        return Math.min(leaveDays, workingDays);
    }

    /**
     * Days actually worked = working days (from command line) - leave days (from CSV).
     * Always consistent with the --days argument.
     */
    public int getDaysWorked(int workingDays) {
        return workingDays - getLeaveDays(workingDays);
    }

    /** Fraction of the month actually worked (for loss-of-pay). */
    protected double attendanceRatio(int workingDays) {
        if (workingDays <= 0) {
            return 0;
        }
        return getDaysWorked(workingDays) / (double) workingDays;
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
    public int getLeaveDays() { return leaveDays; }

    @Override
    public String toString() {
        return id + " - " + name + " (" + getType() + ", " + department + ")";
    }
}
