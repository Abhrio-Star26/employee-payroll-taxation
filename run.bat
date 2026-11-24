@echo off
cd /d "%~dp0"
echo Running Payroll Utility...
java -jar PayrollUtility.jar %*
echo.
echo Payslips are in the "output" folder.
pause
