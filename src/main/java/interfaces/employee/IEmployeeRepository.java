package interfaces.employee;

import java.util.List;
import java.util.Optional;

import models.employeeModels.Employee;

public interface IEmployeeRepository {
    List<Employee> getAllEmployees();
    Optional<Employee> findById(int employeeId);
    Optional<Employee> findByName(String name);
    void save(Employee employee);
    void update(Employee employee);
    void delete(int id);
}