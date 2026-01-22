package factories;

import java.math.BigDecimal;
import java.time.LocalDate;

import models.employeeModels.BaseCashier;
import models.employeeModels.BaseManager;
import models.employeeModels.Employee;



public class EmployeeFactory {

    public static Employee createManagerEmployee(
            int employeeId,
            String name, 
            BigDecimal houtlyRate, 
            String position, 
            boolean isFullTime, 
            LocalDate startedAt,
            int teamSize
        ) {
        return new BaseManager.Builder()
                .employeeId(employeeId)
                .fullName(name)
                .hourlyRate(houtlyRate)
                .position("Manager")
                .isFullTime(isFullTime)
                .startedAt(LocalDate.now())
                .teamSize(teamSize)
                .build();
    }

    public static Employee createCashierEmployee(
            int employeeId, 
            String name, 
            BigDecimal houtlyRate, 
            String position, 
            boolean isFullTime, 
            LocalDate startedAt,
            int registerNumber
        ) {
        return new BaseCashier.Builder()
                .employeeId(employeeId)
                .fullName(name)
                .hourlyRate(houtlyRate)
                .position("Cashier")
                .isFullTime(isFullTime)
                .startedAt(LocalDate.now())
                .registerNumber(registerNumber)
                .shiftCount(0)
                .build();
    }
}