package services.employeeServices;

import java.util.List;
import java.util.Optional;

import models.employeeModels.Employee;
import storage.DataStorage;

public class SimpleEmployeeService {
    private final DataStorage storage;

    public SimpleEmployeeService(DataStorage storage) {
        this.storage = storage;
    }

    // --- FIND EMPLOYEE BY ID ---
    public Optional<Employee> findEmployeeById(int employeeId) {
        return storage.getEmployees().stream()
                .filter(employee -> employee.getId() == employeeId)
                .findFirst();
    }

    // --- GET ALL EMPLOYEES ---
    public List<Employee> getAllEmployees() {
        return storage.getEmployees();
    }
}
