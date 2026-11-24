package payroll.config;

/**
 * Reads settings from command-line arguments of the form --key=value.
 *
 * Supported keys (all optional, defaults shown):
 *   --fy=2025-26               financial year
 *   --month=NOVEMBER           pay month printed on payslips
 *   --days=26                  default working days in the month
 *   --rebate=60000             maximum Sec 87A rebate (default depends on FY)
 *   --input=data/employees.csv employee master file
 *   --output=output            folder for generated payslips
 */
public class PayrollConfig {

    private String financialYear = "2025-26";
    private String month = "NOVEMBER";
    private int workingDays = 26;
    private Double maxRebate = null;          // null = use FY default
    private String inputFile = "data/employees.csv";
    private String outputDir = "output";

    public static PayrollConfig fromArgs(String[] args) {
        PayrollConfig cfg = new PayrollConfig();
        for (String arg : args) {
            if (arg.equals("--help") || arg.equals("-h")) {
                printUsage();
                System.exit(0);
            }
            if (!arg.startsWith("--") || !arg.contains("=")) {
                throw new IllegalArgumentException("Bad argument '" + arg + "'. Use --key=value");
            }
            String key = arg.substring(2, arg.indexOf('=')).toLowerCase();
            String value = arg.substring(arg.indexOf('=') + 1).trim();

            switch (key) {
                case "fy":
                    if (!value.matches("\\d{4}-\\d{2}")) {
                        throw new IllegalArgumentException("FY must look like 2025-26");
                    }
                    cfg.financialYear = value;
                    break;
                case "month":
                    cfg.month = value.toUpperCase();
                    break;
                case "days":
                    cfg.workingDays = Integer.parseInt(value);
                    if (cfg.workingDays < 1 || cfg.workingDays > 31) {
                        throw new IllegalArgumentException("days must be between 1 and 31");
                    }
                    break;
                case "rebate":
                    cfg.maxRebate = Double.parseDouble(value);
                    break;
                case "input":
                    cfg.inputFile = value;
                    break;
                case "output":
                    cfg.outputDir = value;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown option: --" + key);
            }
        }
        return cfg;
    }

    public static void printUsage() {
        System.out.println("Usage: java -cp bin payroll.PayrollApp [options]");
        System.out.println("  --fy=2025-26               financial year (2024-25 or 2025-26)");
        System.out.println("  --month=NOVEMBER           pay month");
        System.out.println("  --days=26                  working days in the month");
        System.out.println("  --rebate=60000             max Sec 87A rebate (optional)");
        System.out.println("  --input=data/employees.csv employee CSV file");
        System.out.println("  --output=output            output folder");
    }

    public String getFinancialYear() { return financialYear; }
    public String getMonth() { return month; }
    public int getWorkingDays() { return workingDays; }
    public Double getMaxRebate() { return maxRebate; }
    public String getInputFile() { return inputFile; }
    public String getOutputDir() { return outputDir; }

    @Override
    public String toString() {
        return "FY=" + financialYear + ", Month=" + month + ", WorkingDays=" + workingDays
                + ", Rebate=" + (maxRebate == null ? "FY default" : maxRebate)
                + ", Input=" + inputFile + ", Output=" + outputDir;
    }
}
