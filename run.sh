#!/bin/sh
cd "$(dirname "$0")"
java -jar PayrollUtility.jar "$@"
echo "Payslips are in the output folder."
