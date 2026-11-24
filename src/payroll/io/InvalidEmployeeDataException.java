package payroll.io;

/**
 * Thrown when a line of the employee CSV file cannot be parsed.
 */
public class InvalidEmployeeDataException extends Exception {

    private static final long serialVersionUID = 1L;

    private final int lineNumber;

    public InvalidEmployeeDataException(int lineNumber, String message) {
        super("Line " + lineNumber + ": " + message);
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
