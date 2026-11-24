package payroll.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Writes payslips and the payroll summary to text files.
 */
public class ReportWriter {

    private final File payslipDir;
    private final File outputDir;

    public ReportWriter(String outputDir) throws IOException {
        this.outputDir = new File(outputDir);
        this.payslipDir = new File(this.outputDir, "payslips");
        if (!payslipDir.exists() && !payslipDir.mkdirs()) {
            throw new IOException("Cannot create folder " + payslipDir.getPath());
        }
    }

    /** Writes one payslip and returns the created file. */
    public File writePaySlip(PaySlip slip) throws IOException {
        File file = new File(payslipDir, slip.fileName());
        writeText(file, slip.format());
        return file;
    }

    /** Writes a one-page register of all employees with totals. */
    public File writeSummary(List<PaySlip> slips, String month, String fy) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line = "------------------------------------------------------------------------------------------";
        sb.append("PAYROLL SUMMARY - ").append(month).append(" (FY ").append(fy).append(")\n");
        sb.append(line).append('\n');
        sb.append(String.format("%-8s %-20s %-12s %14s %14s %14s%n",
                "EmpId", "Name", "Type", "Gross", "Deductions", "Net Pay"));
        sb.append(line).append('\n');

        double totalGross = 0, totalDed = 0, totalNet = 0;
        for (PaySlip s : slips) {
            sb.append(String.format("%-8s %-20s %-12s %,14.2f %,14.2f %,14.2f%n",
                    s.getEmployee().getId(), s.getEmployee().getName(), s.getEmployee().getType(),
                    s.getGrossPay(), s.getTotalDeductions(), s.getNetPay()));
            totalGross += s.getGrossPay();
            totalDed += s.getTotalDeductions();
            totalNet += s.getNetPay();
        }
        sb.append(line).append('\n');
        sb.append(String.format("%-42s %,14.2f %,14.2f %,14.2f%n", "TOTAL (" + slips.size() + " employees)",
                totalGross, totalDed, totalNet));

        File file = new File(outputDir, "payroll_summary_" + month + "_" + fy + ".txt");
        writeText(file, sb.toString());
        return file;
    }

    private void writeText(File file, String text) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(text);
        }
    }
}
