package services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import exceptions.InvalidSettersException;
import interfaces.ICustomerService;
import models.Customer;
import storage.DataStorage;

@Service
public class CustomerService implements ICustomerService {
    private final DataStorage storage;

    public CustomerService(DataStorage storage) {
        this.storage = storage;
    }

    // --- FIND CUSTOMER BY ID ---
    @Override
    public Optional<Customer> findById(int customerId) {
        return storage.getCustomers().stream()
            .filter(customer -> customer.getId() == customerId)
            .findFirst();
    }

    // --- GET ALL CUSTOMERS ---
    @Override
    public List<Customer> getAllCustomers() {
        return storage.getCustomers();
    }

    // --ADD CUSTOMER --- 
    @Override
    public void addCustomer(Customer customer) {
        if (findById(customer.getId()).isPresent()) {
            throw new InvalidSettersException("Customer with this ID " + customer.getId() + " already exists.");
        } 
        storage.addCustomer(customer);
    }

    // --- ADD LOYALTY POINTS ---
    @Override
    public void addLoyaltyPoints(int id, int amount) {
        Customer customer = findById(id)
            .orElseThrow(() -> new InvalidSettersException("Customer with ID " + id + " not found."));

        int newLoyaltyPoints = customer.getLoyaltyPoints() + amount;
        customer.setLoyaltyPoints(newLoyaltyPoints);
    }

    // --- POINTS TO DISCOUNT ---
    @Override
    public double pointsToDiscount(int id, int amount, double price) {
        Customer customer = findById(id)
            .orElseThrow(() -> new InvalidSettersException("Customer with ID " + id + " not found."));

        if (amount <= 0 || amount > customer.getLoyaltyPoints()) {
                throw new InvalidSettersException("Not enough loyalty points or invalid amount.");
            }

        double resultPrice = price - (double) amount;

        int newLoyaltyPoints = customer.getLoyaltyPoints() - amount;
        customer.setLoyaltyPoints(newLoyaltyPoints);
        return resultPrice;
    }
}