package payroll.tax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Slab rules for a financial year (Indian "new tax regime").
 * The financial year comes from the command line, so the same code
 * can compute payroll for different years.
 */
public class TaxRegime {

    private static final double MAX = Double.MAX_VALUE;

    private final String financialYear;
    private final List<TaxSlab> slabs;
    private final double standardDeduction;
    private final double rebateIncomeLimit;   // Sec 87A: income up to this gets a rebate
    private final double defaultMaxRebate;    // maximum rebate amount

    private TaxRegime(String financialYear, List<TaxSlab> slabs, double standardDeduction,
                      double rebateIncomeLimit, double defaultMaxRebate) {
        this.financialYear = financialYear;
        this.slabs = Collections.unmodifiableList(slabs);
        this.standardDeduction = standardDeduction;
        this.rebateIncomeLimit = rebateIncomeLimit;
        this.defaultMaxRebate = defaultMaxRebate;
    }

    /** Returns the slab table for the requested financial year. */
    public static TaxRegime forFinancialYear(String fy) {
        List<TaxSlab> s = new ArrayList<>();
        if ("2024-25".equals(fy)) {
            s.add(new TaxSlab(0, 300000, 0.00));
            s.add(new TaxSlab(300000, 700000, 0.05));
            s.add(new TaxSlab(700000, 1000000, 0.10));
            s.add(new TaxSlab(1000000, 1200000, 0.15));
            s.add(new TaxSlab(1200000, 1500000, 0.20));
            s.add(new TaxSlab(1500000, MAX, 0.30));
            return new TaxRegime(fy, s, 75000, 700000, 25000);
        }
        // Default: FY 2025-26 rules (also used for later years)
        s.add(new TaxSlab(0, 400000, 0.00));
        s.add(new TaxSlab(400000, 800000, 0.05));
        s.add(new TaxSlab(800000, 1200000, 0.10));
        s.add(new TaxSlab(1200000, 1600000, 0.15));
        s.add(new TaxSlab(1600000, 2000000, 0.20));
        s.add(new TaxSlab(2000000, 2400000, 0.25));
        s.add(new TaxSlab(2400000, MAX, 0.30));
        return new TaxRegime(fy, s, 75000, 1200000, 60000);
    }

    public String getFinancialYear() { return financialYear; }
    public List<TaxSlab> getSlabs() { return slabs; }
    public double getStandardDeduction() { return standardDeduction; }
    public double getRebateIncomeLimit() { return rebateIncomeLimit; }
    public double getDefaultMaxRebate() { return defaultMaxRebate; }
}
