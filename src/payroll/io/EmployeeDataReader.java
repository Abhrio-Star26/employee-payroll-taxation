package payroll.io;

import payroll.employee.Employee;
import payroll.employee.EmployeeFactory;
import payroll.employee.EmployeeType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the comma-delimited employee master file.
 *
 * Expected columns:
 *   EmpId,Name,Type,Department,BaseAmount,DaysWorked
 *
 * Lines starting with '#' and blank lines are ignored.
 * Bad lines are skipped and reported, so one typo does not stop the payroll run.
 */
public class EmployeeDataReader {

    private static final int EXPECTED_COLUMNS = 6;

    private final List<String> errors = new ArrayList<>();

    public List<Employee> read(String filePath) throws IOException {
        List<Employee> employees = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNo = 0;
            boolean headerSkipped = false;

            while ((line = br.readLine()) != null) {
                lineNo++;
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }
                if (!headerSkipped) {          // first real line is the header
                    headerSkipped = true;
                    continue;
                }
                try {
                    employees.add(parseLine(line, lineNo));
                } catch (InvalidEmployeeDataException e) {
                    errors.add(e.getMessage());
                }
            }
        }
        return employees;
    }

    private Employee parseLine(String line, int lineNo) throws InvalidEmployeeDataException {
        String[] p = line.split(",");
        if (p.length != EXPECTED_COLUMNS) {
            throw new InvalidEmployeeDataException(lineNo,
                    "expected " + EXPECTED_COLUMNS + " columns but found " + p.length);
        }
        try {
            String id = p[0].trim();
            String name = p[1].trim();
            EmployeeType type = EmployeeType.fromString(p[2]);
            String dept = p[3].trim();
            double base = Double.parseDouble(p[4].trim());
            int days = Integer.parseInt(p[5].trim());

            if (id.isEmpty() || name.isEmpty()) {
                throw new InvalidEmployeeDataException(lineNo, "id/name cannot be empty");
            }
            if (base < 0 || days < 0) {
                throw new InvalidEmployeeDataException(lineNo, "amount/days cannot be negative");
            }
            return EmployeeFactory.create(type, id, name, dept, base, days);

        } catch (NumberFormatException e) {
            throw new InvalidEmployeeDataException(lineNo, "invalid number (" + e.getMessage() + ")");
        } catch (IllegalArgumentException e) {
            throw new InvalidEmployeeDataException(lineNo, e.getMessage());
        }
    }

    public List<String> getErrors() {
        return errors;
    }
}
