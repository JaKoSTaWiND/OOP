package factories;

import java.math.BigDecimal;
import java.time.LocalDate;

import models.employeeModels.Cashier;
import models.employeeModels.Employee;
import models.employeeModels.Manager;

public class EmployeeFactory {

    public static Employee createManager(int id, String name, BigDecimal rate, int teamSize) {
        return new Manager(id, name, rate, LocalDate.now(), teamSize);
    }

    public static Employee createCashier(int id, String name, BigDecimal rate, int regNum) {
        return new Cashier(id, name, rate, LocalDate.now(), regNum);
    }
}