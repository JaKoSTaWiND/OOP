package models.employeeModels;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.immutables.value.Value;

 abstract public class Employee {

    public abstract int employeeId();
    public abstract String fullName();
    public abstract BigDecimal hourlyRate();
    public abstract String position();

    // --- DEFAULTS ---
    @Value.Default
    public boolean isFullTime() {
        return true;
    }
    @Value.Default
    public LocalDate startedAt() {
        return LocalDate.now();
    }

    // --- VALIDATION ---
    @Value.Check
    protected void validateFullName() { // validate fullName
        if (fullName().trim().isEmpty()) {
            throw new IllegalArgumentException("Fullname can not be empty.");
        }
    }
    @Value.Check
    protected void validateHourlyRate() { // validate hourlyRate
        if (hourlyRate().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Hourly rate can not be negative.");
        }
    }
    @Value.Check
    protected void validatePosition() { // validate position
        if (position().trim().isEmpty()) {
            throw new IllegalArgumentException("Position can not be empty.");
        }   
    }

    public abstract String getRoleSpecificInfo();
    public abstract BigDecimal calculateFinalSalary(int workedHours); 

    // --- CALCULATE EXPERIENCE (YEARS) ---
    public long calculateExperience() {
        LocalDate now = LocalDate.now();
        long years = ChronoUnit.YEARS.between(startedAt(), now);
        return years;
    }
}



