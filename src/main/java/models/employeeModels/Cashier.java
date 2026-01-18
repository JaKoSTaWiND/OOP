package models.employeeModels;

import java.math.BigDecimal;
import java.time.LocalDate;

import exceptions.InvalidSettersException;

public class Cashier extends Employee {
    private int registerNumber;
    private int shiftCount;

    public Cashier(int employeeId, String fullName, BigDecimal hourlyRate, LocalDate startedAt, int registerNumber) {
        super(employeeId, fullName, hourlyRate, "Cashier", true, startedAt);
        setRegisterNumber(registerNumber);
        this.shiftCount = 0;
    }

    public int getRegisterNumber() { return registerNumber; }

    public final void setRegisterNumber(int num) {
        if (num <= 0) {
            throw new InvalidSettersException("Register number must be a positive integer!");
        }
        this.registerNumber = num;
    }

    public void openRegister() {
        this.shiftCount++;
        System.out.println("Cashier " + getFullName() + " opened register #" + registerNumber);
    }

    public boolean isExpertCashier() {
        return calculateExperience() >= 1 && shiftCount > 50;
    }

    @Override
    public String getRoleSpecificInfo() {
        return String.format("Register: %d | Shifts: %d", registerNumber, shiftCount);
    }

    @Override
    public BigDecimal calculateFinalSalary(int workedHours) {
        return getHourlyRate().multiply(new BigDecimal(workedHours));
    }
}