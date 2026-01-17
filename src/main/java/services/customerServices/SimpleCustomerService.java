package services.customerServices;

import java.util.List;
import java.util.Optional;

import models.Customer;
import storage.DataStorage;

public class SimpleCustomerService {
    private final DataStorage storage;

    public SimpleCustomerService(DataStorage storage) {
        this.storage = storage;
    }

    // --- FIND CUSTOMER BY ID ---
    public Optional<Customer> findCustomerById(int customerId) {
        return storage.getCustomers().stream()
        .filter(customer -> customer.getId() == customerId)
        .findFirst();
    }

    // --- GET ALL CUSTOMERS ---
    public List<Customer> getAllCustomers() {
        return storage.getCustomers();
    }

    // --- CHECK IF ID IS TAKEN ---
    private boolean isIdTaken(int id) {
        return storage.getCustomers().stream().anyMatch(c -> c.getId() == id);
    }

    // --ADD CUSTOMER --- 
    public void addCustomer(int id, String fullName, String phone, int loyaltePoints, boolean isVip) {
        if (!isIdTaken(id)) {
            Customer newCustomer = new Customer(id, fullName, phone, loyaltePoints, isVip);
            storage.addCustomer(newCustomer);
        } else {
            System.out.println("Customer with ID " + id + " already exists.");
        }
    }

    // --- ADD LOYALTY POINTS ---
    public void addLoyaltyPoints(int id, double amount) {
        findCustomerById(id).ifPresent(customer -> customer.addLoyaltyPoints(amount));
    }
}
