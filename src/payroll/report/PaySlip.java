package payroll.report;

import payroll.employee.Employee;
import payroll.employee.PayComponent;
import payroll.tax.TaxResult;

import java.util.List;

/**
 * Holds everything printed on one employee's payslip
 * and knows how to format itself as text.
 */
public class PaySlip {

    private static final String LINE = "=================================================================";
    private static final String DASH = "-----------------------------------------------------------------";

    private final Employee employee;
    private final String month;
    private final String financialYear;
    private final int workingDays;
    private final List<PayComponent> earnings;
    private final List<PayComponent> deductions;   // excludes income tax
    private final TaxResult tax;

    public PaySlip(Employee employee, String month, String financialYear, int workingDays,
                   List<PayComponent> earnings, List<PayComponent> deductions, TaxResult tax) {
        this.employee = employee;
        this.month = month;
        this.financialYear = financialYear;
        this.workingDays = workingDays;
        this.earnings = earnings;
        this.deductions = deductions;
        this.tax = tax;
    }

    public double getGrossPay() {
        return Employee.sum(earnings);
    }

    public double getTotalDeductions() {
        return Employee.sum(deductions) + tax.getMonthlyTds();
    }

    public double getNetPay() {
        return getGrossPay() - getTotalDeductions();
    }

    public Employee getEmployee() { return employee; }
    public TaxResult getTax() { return tax; }

    /** File name such as EMP001_NOVEMBER_2025-26.txt */
    public String fileName() {
        return employee.getId() + "_" + month + "_" + financialYear + ".txt";
    }

    public String format() {
        StringBuilder sb = new StringBuilder();
        sb.append(LINE).append('\n');
        sb.append(center("ABC TECHNOLOGIES PVT. LTD.")).append('\n');
        sb.append(center("PAYSLIP FOR " + month + "  (FY " + financialYear + ")")).append('\n');
        sb.append(LINE).append('\n');
        sb.append(String.format("Employee ID   : %-18s Type       : %s%n", employee.getId(), employee.getType()));
        sb.append(String.format("Name          : %-18s Department : %s%n", employee.getName(), employee.getDepartment()));
        sb.append(String.format("Working Days  : %-18d Days Worked: %d%n", workingDays, employee.getDaysWorked()));
        sb.append(DASH).append('\n');

        sb.append("EARNINGS\n");
        for (PayComponent c : earnings) {
            sb.append(row(c.getName(), c.getAmount()));
        }
        sb.append(row("GROSS PAY", getGrossPay()));
        sb.append(DASH).append('\n');

        sb.append("DEDUCTIONS\n");
        for (PayComponent c : deductions) {
            sb.append(row(c.getName(), c.getAmount()));
        }
        sb.append(row("Income Tax (TDS)", tax.getMonthlyTds()));
        sb.append(row("TOTAL DEDUCTIONS", getTotalDeductions()));
        sb.append(DASH).append('\n');
        sb.append(row("NET PAY", getNetPay()));
        sb.append(LINE).append('\n');

        sb.append("ANNUAL TAX WORKSHEET (projected)\n");
        sb.append(row("Projected annual income", tax.getGrossAnnualIncome()));
        sb.append(row("Less: Standard deduction", tax.getStandardDeduction()));
        sb.append(row("Taxable income", tax.getTaxableIncome()));
        sb.append(row("Tax as per slabs", tax.getSlabTax()));
        sb.append(row("Less: Rebate u/s 87A", tax.getRebate()));
        sb.append(row("Add: Health & Education cess (4%)", tax.getCess()));
        sb.append(row("Total annual tax", tax.getAnnualTax()));
        sb.append(LINE).append('\n');
        sb.append("This is a computer-generated payslip and needs no signature.\n");
        return sb.toString();
    }

    private static String row(String label, double amount) {
        return String.format("  %-40s Rs. %,14.2f%n", label, amount);
    }

    private static String center(String text) {
        int pad = Math.max(0, (LINE.length() - text.length()) / 2);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pad; i++) {
            sb.append(' ');
        }
        return sb.append(text).toString();
    }
}
