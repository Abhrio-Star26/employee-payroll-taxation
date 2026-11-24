package payroll.tax;

/**
 * Immutable result of an income-tax computation (all values annual,
 * except monthlyTds).
 */
public class TaxResult {

    private final double grossAnnualIncome;
    private final double standardDeduction;
    private final double taxableIncome;
    private final double slabTax;
    private final double rebate;
    private final double cess;
    private final double annualTax;

    public TaxResult(double grossAnnualIncome, double standardDeduction, double taxableIncome,
                     double slabTax, double rebate, double cess, double annualTax) {
        this.grossAnnualIncome = grossAnnualIncome;
        this.standardDeduction = standardDeduction;
        this.taxableIncome = taxableIncome;
        this.slabTax = slabTax;
        this.rebate = rebate;
        this.cess = cess;
        this.annualTax = annualTax;
    }

    public double getGrossAnnualIncome() { return grossAnnualIncome; }
    public double getStandardDeduction() { return standardDeduction; }
    public double getTaxableIncome() { return taxableIncome; }
    public double getSlabTax() { return slabTax; }
    public double getRebate() { return rebate; }
    public double getCess() { return cess; }
    public double getAnnualTax() { return annualTax; }

    /** Tax deducted at source each month. */
    public double getMonthlyTds() {
        return Math.round(annualTax / 12.0 * 100.0) / 100.0;
    }
}
