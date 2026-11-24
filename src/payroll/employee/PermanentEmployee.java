package payroll.employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Permanent staff: Basic + HRA + DA, with PF and Professional Tax deducted.
 * baseAmount = monthly basic salary.
 */
public class PermanentEmployee extends Employee {

    public static final double HRA_RATE = 0.20;          // 20% of basic
    public static final double DA_RATE = 0.12;           // 12% of basic
    public static final double PF_RATE = 0.12;           // 12% of basic
    public static final double PROFESSIONAL_TAX = 200.0; // flat per month

    public PermanentEmployee(String id, String name, String department,
                             double monthlyBasic, int daysWorked) {
        super(id, name, department, monthlyBasic, daysWorked);
    }

    @Override
    public EmployeeType getType() {
        return EmployeeType.PERMANENT;
    }

    private double earnedBasic(int workingDays) {
        return getBaseAmount() * attendanceRatio(workingDays); // loss-of-pay applied
    }

    @Override
    public List<PayComponent> calculateEarnings(int workingDays) {
        double basic = earnedBasic(workingDays);
        List<PayComponent> list = new ArrayList<>();
        list.add(new PayComponent("Basic Pay", basic));
        list.add(new PayComponent("House Rent Allowance", basic * HRA_RATE));
        list.add(new PayComponent("Dearness Allowance", basic * DA_RATE));
        return list;
    }

    @Override
    public List<PayComponent> calculateDeductions(int workingDays) {
        List<PayComponent> list = new ArrayList<>();
        list.add(new PayComponent("Provident Fund (12%)", earnedBasic(workingDays) * PF_RATE));
        list.add(new PayComponent("Professional Tax", PROFESSIONAL_TAX));
        return list;
    }

    @Override
    public double projectedAnnualIncome(int workingDays) {
        double fullMonth = getBaseAmount() * (1 + HRA_RATE + DA_RATE);
        return fullMonth * 12;
    }

    @Override
    public boolean isStandardDeductionEligible() {
        return true;
    }
}
