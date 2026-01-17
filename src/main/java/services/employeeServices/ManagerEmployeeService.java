package services.employeeServices;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import models.employeeModels.Manager;
import storage.DataStorage;

public class ManagerEmployeeService {
    private final DataStorage storage;

    public ManagerEmployeeService(DataStorage storage) {
        this.storage = storage;
    }

    private boolean isIdTaken(int id) {
        return storage.getEmployees().stream().anyMatch(e -> e.getId() == id);
    }

    public void addManager(int id, String fullName, BigDecimal hourlyRate, LocalDate startDate, int teamSize) {
        if (isIdTaken(id)) {
            System.out.println("Employee with ID " + id + " already exists.");
        } else {
            Manager manager = new Manager(id, fullName, hourlyRate, startDate, teamSize);
            storage.addEmployee(manager);
        }
    }

    public List<Manager> getAllManagers() {
        return storage.getEmployees().stream()
                .filter(e -> e instanceof Manager)
                .map(e -> (Manager) e)
                .toList();
    }

}
