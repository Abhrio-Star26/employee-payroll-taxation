package payroll.employee;

/**
 * The three employment tiers supported by the utility.
 */
public enum EmployeeType {
    PERMANENT,
    CONTRACTUAL,
    INTERN;

    /** Converts text from the CSV file (any case) into an EmployeeType. */
    public static EmployeeType fromString(String text) {
        try {
            return EmployeeType.valueOf(text.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unknown employee type: " + text);
        }
    }
}
