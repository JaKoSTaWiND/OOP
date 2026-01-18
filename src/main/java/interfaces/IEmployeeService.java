package interfaces;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import models.employeeModels.Employee;

public interface IEmployeeService {
    void addEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> findById(int employeeId);
    
    BigDecimal calculatePayroll(int employeeId, int workedHours, double bonus);

    List<Employee> getEmployeesByType(Class<? extends Employee> type); // Get employees by their child class
}
