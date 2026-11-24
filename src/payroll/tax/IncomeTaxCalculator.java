package payroll.tax;

import payroll.employee.Employee;

/**
 * Applies slab rates, Sec 87A rebate and 4% health &amp; education cess.
 *
 * Steps:
 *  1. projected annual income
 *  2. minus standard deduction (salaried only)
 *  3. slab-wise tax
 *  4. minus rebate (if taxable income within the rebate limit)
 *  5. plus cess
 */
public class IncomeTaxCalculator {

    public static final double CESS_RATE = 0.04;

    private final TaxRegime regime;
    private final double maxRebate;

    public IncomeTaxCalculator(TaxRegime regime, double maxRebate) {
        this.regime = regime;
        this.maxRebate = maxRebate;
    }

    public TaxResult calculate(Employee emp, int workingDays) {
        double gross = emp.projectedAnnualIncome(workingDays);

        double stdDeduction = emp.isStandardDeductionEligible()
                ? Math.min(regime.getStandardDeduction(), gross) : 0;
        double taxable = gross - stdDeduction;

        double slabTax = 0;
        for (TaxSlab slab : regime.getSlabs()) {
            slabTax += slab.taxFor(taxable);
        }

        double rebate = 0;
        if (taxable <= regime.getRebateIncomeLimit()) {
            rebate = Math.min(slabTax, maxRebate);
        }

        double taxAfterRebate = slabTax - rebate;
        double cess = taxAfterRebate * CESS_RATE;
        double annualTax = round(taxAfterRebate + cess);

        return new TaxResult(round(gross), round(stdDeduction), round(taxable),
                round(slabTax), round(rebate), round(cess), annualTax);
    }

    private static double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
