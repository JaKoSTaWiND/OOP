package models.employeeModels;

import java.math.BigDecimal;

import org.immutables.value.Value;

import models.ImmutableStyle;

@ImmutableStyle
@Value.Immutable
public abstract class Manager extends Employee {

    public abstract int teamSize();

    // --- DERIVED ---
    @Value.Derived
    public boolean canApproveLargeDiscount() {
        return calculateExperience() >= 3; 
    }

    @Override
    public String getRoleSpecificInfo() {
        return "Team Size: " + teamSize();
    }       

    @Override
    public BigDecimal calculateFinalSalary(int workedHours) {
        BigDecimal base = hourlyRate().multiply(new BigDecimal(workedHours));
        BigDecimal teamBonus = new BigDecimal(teamSize()).multiply(new BigDecimal("50.00"));
        return base.add(teamBonus);
    }
}