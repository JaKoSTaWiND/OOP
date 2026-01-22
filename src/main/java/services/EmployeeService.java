package services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import interfaces.IEmployeeService;
import models.employeeModels.Employee;
import storage.DataStorage;

@Service
public class EmployeeService implements IEmployeeService {
    private final DataStorage storage;

    public EmployeeService(DataStorage storage) {
        this.storage = storage;
    }

    // --- ADD EMPLOYEE ---
    @Override
    public void addEmployee(Employee employee) {
        if (findById(employee.employeeId()).isPresent()) {
            throw new IllegalArgumentException("Employee with ID " + employee.employeeId() + " already exists.");
        }
        storage.getEmployees().add(employee);
        }

    // --- FIND EMPLOYEE BY ID ---
    @Override
    public Optional<Employee> findById(int employeeId) {
        return storage.getEmployees().stream()
                .filter(employee -> employee.employeeId() == employeeId)
                .findFirst();
    }

    // --- GET ALL EMPLOYEES ---
    @Override
    public List<Employee> getAllEmployees() {
        return storage.getEmployees();
    }

    // --- CALCULATE PAYROLL ---
    @Override
    public BigDecimal calculatePayroll(int id, int hours, double bonus) {
        return findById(id)
                .map(emp -> emp.calculateFinalSalary(hours).add(BigDecimal.valueOf(bonus)))
                .orElse(BigDecimal.ZERO);
    }

    // --- GET EMPLOYEES BY TYPE ---
    @Override
    public List<Employee> getEmployeesByType(Class<? extends Employee> type) { // Get employees by their child class
        return storage.getEmployees().stream()
                .filter(type::isInstance)
                .toList();
    }
}