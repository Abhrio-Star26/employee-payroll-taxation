package payroll.tax;

/**
 * One income slab, e.g. income from 4,00,001 to 8,00,000 taxed at 5%.
 * upperLimit = Double.MAX_VALUE for the last (open-ended) slab.
 */
public class TaxSlab {

    private final double lowerLimit;
    private final double upperLimit;
    private final double rate; // 0.05 means 5%

    public TaxSlab(double lowerLimit, double upperLimit, double rate) {
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
        this.rate = rate;
    }

    /** Tax payable on the part of income that falls inside this slab. */
    public double taxFor(double income) {
        if (income <= lowerLimit) {
            return 0;
        }
        double taxablePart = Math.min(income, upperLimit) - lowerLimit;
        return taxablePart * rate;
    }

    public double getLowerLimit() { return lowerLimit; }
    public double getUpperLimit() { return upperLimit; }
    public double getRate() { return rate; }
}
