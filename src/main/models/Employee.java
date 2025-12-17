package main.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Employee {
    private int employeeId;
    private String fullName;
    private BigDecimal hourlyRate;
    private String position;
    private boolean isFullTime;
    private LocalDate startedAt;

    // --- CONSTRUCTION ---
    public Employee(int employeeId, String fullName, BigDecimal hourlyRate, String position, boolean isFullTime, LocalDate startedAt) {
        this.employeeId = employeeId;
        this.fullName = fullName;
        this.hourlyRate = hourlyRate;
        this.position = position;
        this.isFullTime = isFullTime;
        this.startedAt = startedAt;
    }

    // --- DEFAULT CONSTRUCTION ---
    public Employee() {
        this.isFullTime = true;
        this.startedAt = LocalDate.now();
    }

    // --- GETTERS ---
    public String getFullName() { return fullName; }
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public String getPosition() { return position; }
    public boolean getIsFullTime() { return isFullTime; }
    public LocalDate getStartedAt() { return startedAt;}

    // --- SETTERS ---
    public void setFullName(String fullName) {
        if (fullName != null) {
            this.fullName = fullName;
        } else {
            System.out.println("Invalid full name");
        }
    }

    public void setHourlyRate(BigDecimal hourlyRate) {
        if (hourlyRate.compareTo(BigDecimal.ZERO) >= 0) {
            this.hourlyRate = hourlyRate;
        } else {
            System.out.println("Invalid hourly rate");
        }
    }

    public void setPosition(String position) {
        if (position != null) {
            this.position = position;
        } else {
            System.out.println("Invalid position");
        }
    }

    public void setIsFullTime(boolean isFullTime) {
        this.isFullTime = isFullTime;
    }



    // --- CALCULATE MOUTHLY SALARY ---
    public BigDecimal calculateMouthlySalary(double bonusPercentage, int workedHours) {
        BigDecimal hours = new BigDecimal(workedHours);
        BigDecimal bonus = new BigDecimal(bonusPercentage);

        BigDecimal baseSalary = hourlyRate.multiply(hours);
        BigDecimal resultSalary = baseSalary.add(bonus).setScale(2, RoundingMode.HALF_UP);

        System.out.println("Result salary: " + resultSalary);
        return resultSalary;
    }

    // --- CALCULATE EXPERIENCE (YEARS) ---
    public long calculateExperience() {
        LocalDate now = LocalDate.now();
        long years = ChronoUnit.YEARS.between(this.startedAt, now);
        System.out.println("Employee experience: " + years + " " + "years");
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
