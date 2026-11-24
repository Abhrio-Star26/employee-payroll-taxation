package payroll.employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Contractual staff are paid per day worked. No HRA/DA/PF.
 * baseAmount = daily rate.
 */
public class ContractualEmployee extends Employee {

    public ContractualEmployee(String id, String name, String department,
                               double dailyRate, int daysWorked) {
        super(id, name, department, dailyRate, daysWorked);
    }

    @Override
    public EmployeeType getType() {
        return EmployeeType.CONTRACTUAL;
    }

    @Override
    public List<PayComponent> calculateEarnings(int workingDays) {
        int paidDays = Math.min(getDaysWorked(), workingDays);
        List<PayComponent> list = new ArrayList<>();
        list.add(new PayComponent(
                "Contract Pay (" + paidDays + " days x " + getBaseAmount() + ")",
                paidDays * getBaseAmount()));
        return list;
    }

    @Override
    public List<PayComponent> calculateDeductions(int workingDays) {
        return new ArrayList<>(); // no statutory deductions other than tax
    }

    @Override
    public double projectedAnnualIncome(int workingDays) {
        return getBaseAmount() * workingDays * 12;
    }

    @Override
    public boolean isStandardDeductionEligible() {
        return false;
    }
}
