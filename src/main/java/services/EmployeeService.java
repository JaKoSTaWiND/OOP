package services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import exceptions.InvalidSettersException;
import interfaces.employee.IEmployeeRepository;
import interfaces.employee.IEmployeeService;
import models.employeeModels.Employee;

/**
 * Core implementation of the {@link IEmployeeService} interface.
 * <p>
 * This service manages business operations related to employees, including 
 * hiring (persistence), personnel updates, role-specific filtering, and 
 * payroll calculations. It bridges the UI layer with the {@link IEmployeeRepository}.
 * </p>
 * Key features include:
 * <ul>
 * <li>Transactional payroll processing.</li>
 * <li>Validation of employee existence before updates/deletions.</li>
 * <li>Integration with persistent storage via jOOQ repository.</li>
 * </ul>
 * 
 * @see IEmployeeService
 * @see IEmployeeRepository
 */
@Service
public class EmployeeService implements IEmployeeService {

    private final IEmployeeRepository repository;
    public EmployeeService(IEmployeeRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves an employee by their unique identifier.
     * 
     * @param employeeId the database ID.
     * @return an {@link Optional} containing the employee, or empty otherwise.
     */
    @Override
    public Optional<Employee> findById(int employeeId) {
        return repository.findById(employeeId);
    }

    /**
     * Finds an employee by their name.
     * 
     * @param name the name of the employee.
     * @return an {@link Optional} containing the employee if found, or empty otherwise.
     */
    @Override
    public Optional<Employee> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return repository.getAllEmployees();
    }

    /**
     * Hires a new employee and persists their record.
     * 
     * @param employee the employee instance to add.
     */
    @Override
    @Transactional
    public void addEmployee(Employee employee) {        
        if (repository.findById(employee.employeeId()).isPresent()) {
            throw new InvalidSettersException("Employee with ID " + employee.employeeId() + " already exists.");
        }
        repository.save(employee);
    }

    /**
     * Removes an employee record from the system.
     * 
     * @param id the ID of the employee to dismiss.
     */
    @Override
    @Transactional
    public void deleteEmployee(int id) {
        if (repository.findById(id).isEmpty()) {
            throw new InvalidSettersException("Employee with ID " + id + " not found.");
        }
        repository.delete(id);
    }

    /**
     * Updates an existing employee's information.
     * 
     * @param employee the employee instance with modified data.
     */
    @Override
    @Transactional
    public void updateEmployee(Employee employee) {
        if (employee == null || employee.employeeId() <= 0) {
            throw new InvalidSettersException("Invalid employee data for update.");
        }

        if (repository.findById(employee.employeeId()).isEmpty()) {
            throw new InvalidSettersException("Employee with ID " + employee.employeeId() + " does not exist.");
        }

        repository.update(employee);
    }

    /**
     * Calculates the final salary for a specific employee including bonuses.
     * <p>
     * This method utilizes the domain model's internal logic to compute base 
     * pay and adds the provided bonus.
     * </p>
     * 
     * @param id    the employee's ID.
     * @param hours the number of hours worked in the period.
     * @param bonus the additional bonus amount to add.
     * @return the total calculated salary as {@link BigDecimal}.
     */
    @Override
    public BigDecimal calculatePayroll(int id, int hours, double bonus) {
        return repository.findById(id)
                .map(emp -> emp.calculateFinalSalary(hours).add(BigDecimal.valueOf(bonus)))
                .orElseThrow(() -> new InvalidSettersException("Employee with ID " + id + " not found."));
    }

    /**
     * Filters employees by their specific implementation type (e.g., Manager, Cashier).
     * 
     * @param type the class type to filter by.
     * @return a list of employees matching the specified type.
     */
    @Override
    public List<Employee> getEmployeesByType(Class<? extends Employee> type) {
        return repository.getAllEmployees().stream()
                .filter(type::isInstance)
                .toList();
    }
}