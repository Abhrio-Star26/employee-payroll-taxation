package payroll.employee;

import java.util.ArrayList;
import java.util.List;

/**
 * Interns receive a fixed monthly stipend, pro-rated by attendance.
 * baseAmount = monthly stipend.
 */
public class Intern extends Employee {

    public Intern(String id, String name, String department,
                  double monthlyStipend, int daysWorked) {
        super(id, name, department, monthlyStipend, daysWorked);
    }

    @Override
    public EmployeeType getType() {
        return EmployeeType.INTERN;
    }

    @Override
    public List<PayComponent> calculateEarnings(int workingDays) {
        List<PayComponent> list = new ArrayList<>();
        list.add(new PayComponent("Stipend", getBaseAmount() * attendanceRatio(workingDays)));
        return list;
    }

    @Override
    public List<PayComponent> calculateDeductions(int workingDays) {
        return new ArrayList<>();
    }

    @Override
    public double projectedAnnualIncome(int workingDays) {
        return getBaseAmount() * 12;
    }

    @Override
    public boolean isStandardDeductionEligible() {
        return false;
    }
}
