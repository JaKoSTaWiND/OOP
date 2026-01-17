package models.employeeModels;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Manager extends Employee {
    private int teamSize;

    public Manager(int employeeId, String fullName, BigDecimal hourlyRate, LocalDate startedAt, int teamSize) {
        super(employeeId, fullName, hourlyRate, "Manager", true, startedAt);
        this.teamSize = teamSize;
    }

    public int getTeamSize() { return teamSize; }
    public void setTeamSize(int size) { this.teamSize = size; }

    @Override
    public BigDecimal calculateMouthlySalary(double bonusPercentage, int workedHours) {
        /* 
        1. Calculate standard salary
        2. Add team bonus (count of team members * 50$ for each member)      
        */
        BigDecimal standardSalary = super.calculateMouthlySalary(bonusPercentage, workedHours);
        BigDecimal teamBonus = new BigDecimal(teamSize).multiply(new BigDecimal("50.00")); 
        return standardSalary.add(teamBonus);
    }

    public void rewardTeam() {
        System.out.println("Manager " + getFullName() + " awarded a bonus to " + teamSize + " employees.");
    }

    public boolean canApproveLargeDiscount() {
        return calculateExperience() >= 3; 
    }
}