package models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Cashier extends Employee {
    private int registerNumber;
    private int shiftCount;

    public Cashier(int employeeId, String fullName, BigDecimal hourlyRate, LocalDate startedAt, int registerNumber) {
        super(employeeId, fullName, hourlyRate, "Cashier", true, startedAt);
        this.registerNumber = registerNumber;
        this.shiftCount = 0;
    }

    public int getRegisterNumber() { return registerNumber; }
    public void setRegisterNumber(int num) { this.registerNumber = num; }

    public void openRegister() {
        this.shiftCount++;
        System.out.println("Cashier " + getFullName() + " opened register #" + registerNumber);
    }

    public boolean isExpertCashier() {
        return calculateExperience() >= 1 && shiftCount > 50;
    }
}