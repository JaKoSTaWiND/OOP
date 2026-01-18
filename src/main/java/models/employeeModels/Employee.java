package models.employeeModels;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import exceptions.InvalidSettersException;

 abstract public class Employee {
    private int employeeId;
    private String fullName;
    private BigDecimal hourlyRate;
    private String position;
    private boolean isFullTime;
    private LocalDate startedAt;

    // --- CONSTRUCTION ---
    public Employee(int employeeId, String fullName, BigDecimal hourlyRate, String position, boolean isFullTime, LocalDate startedAt) {
        this.employeeId = employeeId;
        setFullName(fullName);
        setHourlyRate(hourlyRate);
        setPosition(position);
        setIsFullTime(isFullTime);
        setStartedAt(startedAt);
    }

    // --- DEFAULT CONSTRUCTION ---
    public Employee() {
        this.isFullTime = true;
        this.startedAt = LocalDate.now();
    }

    // --- GETTERS ---
    public int getId() { return employeeId; }
    public String getFullName() { return fullName; }
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public String getPosition() { return position; }
    public boolean getIsFullTime() { return isFullTime; }
    public LocalDate getStartedAt() { return startedAt;}

    // --- SETTERS ---
    public final void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new InvalidSettersException("Fullname can not be empty.");
        }
            this.fullName = fullName;
    }

    public final void setHourlyRate(BigDecimal hourlyRate) {
        if (hourlyRate == null || hourlyRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidSettersException("Hourly rate can not be negative or missing.");
        }
        this.hourlyRate = hourlyRate;
    }

    public final void setPosition(String position) {
        if (position == null || position.trim().isEmpty()) {
            throw new InvalidSettersException("Position can not be empty.");
        }
        this.position = position;
    }

    public final void setIsFullTime(boolean isFullTime) {
        this.isFullTime = isFullTime;
    }

    public final void setStartedAt(LocalDate startedAt) {
        this.startedAt = startedAt;
    }

    public abstract String getRoleSpecificInfo();
    public abstract BigDecimal calculateFinalSalary(int workedHours); 

    // --- CALCULATE EXPERIENCE (YEARS) ---
    public long calculateExperience() {
        LocalDate now = LocalDate.now();
        long years = ChronoUnit.YEARS.between(this.startedAt, now);
        return years;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId:" + employeeId +
                "Full name:" + fullName +
                "Hourly rate:" + hourlyRate +
                "Position:" + position +
                "Is full time:" + isFullTime +
                "Started at:" + startedAt +
                "}";
    }
}



