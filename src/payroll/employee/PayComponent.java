package payroll.employee;

/**
 * One line item on a payslip, e.g. "Basic Pay = 50000.00"
 * or "Provident Fund = 6000.00".
 */
public class PayComponent {

    private final String name;
    private final double amount;

    public PayComponent(String name, double amount) {
        this.name = name;
        this.amount = Math.round(amount * 100.0) / 100.0; // keep 2 decimals
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }
}
