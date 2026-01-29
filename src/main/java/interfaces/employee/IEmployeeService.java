package interfaces.employee;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import models.employeeModels.Employee;

public interface IEmployeeService {
    List<Employee> getAllEmployees();
    Optional<Employee> findById(int employeeId);
    Optional<Employee> findByName(String name);

    void addEmployee(Employee employee);
    void updateEmployee(Employee employee);
    void deleteEmployee(int id);
    
    BigDecimal calculatePayroll(int employeeId, int workedHours, double bonus);

    List<Employee> getEmployeesByType(Class<? extends Employee> type); // Get employees by their child class
}
