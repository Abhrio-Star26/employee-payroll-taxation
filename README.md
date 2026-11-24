# Employee Payroll and Income Tax Computation Utility

OOP Lab (PCC-CS 593) project – Group 7, KGEC.

A modular, package-structured Java application that computes monthly salaries for
**permanent**, **contractual** and **intern** employees, applies income-tax deductions
based on income slabs, and generates itemized payslips as text files.

## Concepts demonstrated
| Concept | Where |
|---|---|
| Packages | `payroll.employee`, `payroll.tax`, `payroll.report`, `payroll.io`, `payroll.config` |
| Inheritance & polymorphism | `Employee` (abstract) → `PermanentEmployee`, `ContractualEmployee`, `Intern` |
| Command-line arguments | `PayrollConfig` (`--fy`, `--rebate`, `--days`, `--month`, `--input`, `--output`) |
| File input | `EmployeeDataReader` reads `data/employees.csv` |
| File output | `ReportWriter` writes payslips + summary to `output/` |
| Custom exception | `InvalidEmployeeDataException` |
| Enum, Factory pattern | `EmployeeType`, `EmployeeFactory` |

## Project structure
```
payroll-utility/
├── data/employees.csv
├── src/payroll/
│   ├── PayrollApp.java
│   ├── config/PayrollConfig.java
│   ├── employee/  Employee, PermanentEmployee, ContractualEmployee, Intern,
│   │              EmployeeType, EmployeeFactory, PayComponent
│   ├── io/        EmployeeDataReader, InvalidEmployeeDataException
│   ├── tax/       TaxSlab, TaxRegime, TaxResult, IncomeTaxCalculator
│   └── report/    PaySlip, PaySlipGenerator, ReportWriter
└── output/        (generated)
```

## How to compile and run (JDK 8 or later)
Run all commands from the project root folder (the one containing `src` and `data`).

Linux / macOS:
```bash
javac -d bin $(find src -name "*.java")
java -cp bin payroll.PayrollApp --fy=2025-26 --month=NOVEMBER --days=26
```
Windows (Command Prompt):
```bat
mkdir bin
javac -d bin -sourcepath src src\payroll\PayrollApp.java
java -cp bin payroll.PayrollApp --fy=2025-26 --month=NOVEMBER --days=26
```
Windows (PowerShell):
```powershell
mkdir bin -Force
javac -d bin (Get-ChildItem -Path src -Recurse -Filter *.java).FullName
java -cp bin payroll.PayrollApp --fy=2025-26 --month=NOVEMBER --days=26
```

> The Windows commands above also work when the project path contains spaces.

### Command-line options
| Option | Default | Meaning |
|---|---|---|
| `--fy` | `2025-26` | Financial year (`2024-25` or `2025-26` slabs) |
| `--month` | `NOVEMBER` | Pay month |
| `--days` | `26` | Working days in the month |
| `--rebate` | FY default | Maximum Sec 87A rebate |
| `--input` | `data/employees.csv` | Employee file |
| `--output` | `output` | Output folder |

## Pay rules
- **Permanent:** Basic + HRA (20%) + DA (12%); PF 12% of basic, Professional Tax ₹200; loss of pay by attendance; standard deduction allowed.
- **Contractual:** daily rate × days worked; no PF/allowances.
- **Intern:** monthly stipend pro-rated by attendance.
- **Tax:** new-regime slabs for the FY → rebate u/s 87A → 4% cess → annual tax ÷ 12 = monthly TDS.

> Tax rules are simplified for academic purposes (no marginal relief, surcharge or other exemptions).

## Sample output
Payslips are written to `output/payslips/EMP001_NOVEMBER_2025-26.txt`, etc.,
and a register to `output/payroll_summary_NOVEMBER_2025-26.txt`.

### Viewing all payslips at once
After running the program, print every payslip one after another with a single command
(works in both Command Prompt and PowerShell):
```bat
type output\payslips\*.txt
```
To view only one month's payslips, or to view the summary register:
```bat
type output\payslips\*NOVEMBER_2025-26.txt
type output\payroll_summary_NOVEMBER_2025-26.txt
```