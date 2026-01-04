package models;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Manager extends Employee {
    private int teamSize;
    private BigDecimal monthlyBonus;

    public Manager(int employeeId, String fullName, BigDecimal hourlyRate, LocalDate startedAt, int teamSize) {
        super(employeeId, fullName, hourlyRate, "Manager", true, startedAt);
        this.teamSize = teamSize;
        this.monthlyBonus = new BigDecimal("500.00");
    }

    public int getTeamSize() { return teamSize; }
    public void setTeamSize(int size) { this.teamSize = size; }

    public void rewardTeam() {
        System.out.println("Manager " + getFullName() + " awarded a bonus to " + teamSize + " employees.");
    }

    public boolean canApproveLargeDiscount() {
        return calculateExperience() >= 3; 
    }
}