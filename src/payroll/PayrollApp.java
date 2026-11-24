package payroll;

import payroll.config.PayrollConfig;
import payroll.employee.Employee;
import payroll.io.EmployeeDataReader;
import payroll.report.PaySlip;
import payroll.report.PaySlipGenerator;
import payroll.report.ReportWriter;
import payroll.tax.IncomeTaxCalculator;
import payroll.tax.TaxRegime;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Entry point of the Employee Payroll and Income Tax Computation Utility.
 *
 * Flow: read args -> read CSV -> compute pay + tax -> write payslips -> write summary
 */
public class PayrollApp {

    public static void main(String[] args) {
        System.out.println("=== Employee Payroll & Income Tax Computation Utility ===");

        // 1. Command-line configuration
        PayrollConfig config;
        try {
            config = PayrollConfig.fromArgs(args);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            PayrollConfig.printUsage();
            return;
        }
        System.out.println("Config: " + config);

        // 2. Tax rules for the chosen financial year
        TaxRegime regime = TaxRegime.forFinancialYear(config.getFinancialYear());
        double maxRebate = (config.getMaxRebate() != null)
                ? config.getMaxRebate() : regime.getDefaultMaxRebate();
        IncomeTaxCalculator taxCalc = new IncomeTaxCalculator(regime, maxRebate);

        // 3. Read employee master data
        EmployeeDataReader reader = new EmployeeDataReader();
        List<Employee> employees;
        try {
            employees = reader.read(config.getInputFile());
        } catch (IOException e) {
            System.err.println("Cannot read input file: " + e.getMessage());
            return;
        }
        System.out.println("Loaded " + employees.size() + " employee(s).");
        for (String err : reader.getErrors()) {
            System.out.println("  [SKIPPED] " + err);
        }

        // 4. Generate and write payslips
        PaySlipGenerator generator = new PaySlipGenerator(taxCalc, config.getMonth(),
                config.getFinancialYear(), config.getWorkingDays());
        List<PaySlip> slips = new ArrayList<>();
        try {
            ReportWriter writer = new ReportWriter(config.getOutputDir());
            for (Employee emp : employees) {
                PaySlip slip = generator.generate(emp);
                slips.add(slip);
                File f = writer.writePaySlip(slip);
                System.out.printf("  %-8s %-20s Net Pay: Rs. %,12.2f -> %s%n",
                        emp.getId(), emp.getName(), slip.getNetPay(), f.getPath());
            }
            File summary = writer.writeSummary(slips, config.getMonth(), config.getFinancialYear());
            System.out.println("Summary written to " + summary.getPath());
        } catch (IOException e) {
            System.err.println("Cannot write output: " + e.getMessage());
            return;
        }
        System.out.println("Done.");
    }
}
