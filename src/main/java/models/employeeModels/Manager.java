package models.employeeModels;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Manager extends Employee {
    private int teamSize;

    public Manager(int employeeId, String fullName, BigDecimal hourlyRate, LocalDate startedAt, int teamSize) {
        super(employeeId, fullName, hourlyRate, "Manager", true, startedAt);
        this.teamSize = teamSize;
    }

    public void setTeamSize(int size) { this.teamSize = size; }

    public void rewardTeam() {
        System.out.println("Manager " + getFullName() + " awarded a bonus to " + teamSize + " employees.");
    }

    public boolean canApproveLargeDiscount() {
        return calculateExperience() >= 3; 
    }

    @Override
    public String getRoleSpecificInfo() {
        return "Team Size: " + teamSize;
    }

    @Override
    public BigDecimal calculateFinalSalary(int workedHours) {
        BigDecimal base = getHourlyRate().multiply(new BigDecimal(workedHours));
        BigDecimal teamBonus = new BigDecimal(teamSize).multiply(new BigDecimal("50.00"));
        return base.add(teamBonus);
    }
}