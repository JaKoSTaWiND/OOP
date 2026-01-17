package services.employeeServices;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import models.employeeModels.Cashier;
import storage.DataStorage;

public class CashierEmployeeService {
    private final DataStorage storage;

    public CashierEmployeeService(DataStorage storage) {
        this.storage = storage;
    }

    public boolean isIdTaken (int id) {
        return storage.getEmployees().stream().anyMatch(e -> e.getId() == id);
    }

    public void addCashier (int id, String fullName, BigDecimal hourlyRate, LocalDate startedAt, int registerNumbers) {
        if (isIdTaken(id)) {
            System.out.println("Employee with ID " + id + " already exists.");
        } else {
            Cashier cashier = new Cashier(id, fullName, hourlyRate, startedAt, registerNumbers);
            storage.addEmployee(cashier);
        }
    }

    public List<Cashier> getAllCashiers() {
        return storage.getEmployees().stream()
            .filter(e -> e instanceof Cashier)
            .map(e -> (Cashier) e)
            .toList();
    }
}
