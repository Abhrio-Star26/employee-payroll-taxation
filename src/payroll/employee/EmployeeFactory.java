package payroll.employee;

/**
 * Factory: creates the correct subclass based on the employee type.
 */
public final class EmployeeFactory {

    private EmployeeFactory() { }

    public static Employee create(EmployeeType type, String id, String name,
                                  String department, double baseAmount, int leaveDays) {
        switch (type) {
            case PERMANENT:
                return new PermanentEmployee(id, name, department, baseAmount, leaveDays);
            case CONTRACTUAL:
                return new ContractualEmployee(id, name, department, baseAmount, leaveDays);
            case INTERN:
                return new Intern(id, name, department, baseAmount, leaveDays);
            default:
                throw new IllegalArgumentException("Unsupported type: " + type);
        }
    }
}
