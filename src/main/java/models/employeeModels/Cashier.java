package models.employeeModels;

import java.math.BigDecimal;

import org.immutables.value.Value;

import models.ImmutableStyle;

@ImmutableStyle
@Value.Immutable
public abstract class Cashier extends Employee {

    public abstract int registerNumber();
    public abstract int shiftCount();

    // --- VALIDATION ---
    @Value.Check
    protected void validateRegisterNumber() { // validate registerNumber
        if (registerNumber() <= 0) {
            throw new IllegalArgumentException("Register number must be a positive integer!");
        }
    }
    @Value.Check
    protected void validateShiftCount() { // validate shiftCount
        if (shiftCount() < 0) {
            throw new IllegalArgumentException("Shift count cannot be negative!");
        }
    }


    public Cashier withIncrementedShift() {
        return BaseCashier.copyOf(this).withShiftCount(shiftCount() + 1);
    }

    public boolean isExpertCashier() {
        return calculateExperience() >= 1 && shiftCount() > 50;
    }

    @Override
    public String getRoleSpecificInfo() {
        return String.format("Register: %d | Shifts: %d", registerNumber(), shiftCount());
    }

    @Override
    public BigDecimal calculateFinalSalary(int workedHours) {
        return hourlyRate().multiply(new BigDecimal(workedHours));
    }
}