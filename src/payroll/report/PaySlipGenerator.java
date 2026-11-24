package payroll.report;

import payroll.employee.Employee;
import payroll.tax.IncomeTaxCalculator;
import payroll.tax.TaxResult;

/**
 * Builds a PaySlip for an employee: earnings + deductions + tax.
 */
public class PaySlipGenerator {

    private final IncomeTaxCalculator taxCalculator;
    private final String month;
    private final String financialYear;
    private final int workingDays;

    public PaySlipGenerator(IncomeTaxCalculator taxCalculator, String month,
                            String financialYear, int workingDays) {
        this.taxCalculator = taxCalculator;
        this.month = month;
        this.financialYear = financialYear;
        this.workingDays = workingDays;
    }

    public PaySlip generate(Employee emp) {
        TaxResult tax = taxCalculator.calculate(emp, workingDays);
        return new PaySlip(emp, month, financialYear, workingDays,
                emp.calculateEarnings(workingDays),     // polymorphic call
                emp.calculateDeductions(workingDays),   // polymorphic call
                tax);
    }
}
