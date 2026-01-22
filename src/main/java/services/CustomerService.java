package services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import exceptions.InvalidSettersException;
import interfaces.ICustomerService;
import models.customerModels.BaseCustomer;
import models.customerModels.Customer;
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
            .filter(customer -> customer.customerId() == customerId)
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
        if (findById(customer.customerId()).isPresent()) {
            throw new InvalidSettersException("Customer with this ID " + customer.customerId() + " already exists.");
        } 
        storage.addCustomer(customer);
    }

    // --- ADD LOYALTY POINTS ---
    // @Override
    // public void addLoyaltyPoints(int id, int amount) {
    //     Customer customer = findById(id)
    //         .orElseThrow(() -> new InvalidSettersException("Customer with ID " + id + " not found."));

    //     int newLoyaltyPoints = customer.loyaltyPoints() + amount;
    //     customer.loyaltyPoints() = newLoyaltyPoints;
    // }

    @Override
    public void addLoyaltyPoints(int customerId, int amount) {
        Customer customer = findById(customerId)
            .orElseThrow(() -> new InvalidSettersException("Customer with ID " + customerId + " not found."));

        if (amount < 0) {
            throw new InvalidSettersException("Amount to add cannot be negative.");
        }

        int newLoyaltyPoints = customer.loyaltyPoints() + amount;

        Customer updatedCustomer = BaseCustomer.copyOf(customer)
            .withLoyaltyPoints(newLoyaltyPoints);

        storage.updateCustomer(customer, updatedCustomer);
    }

    // // --- POINTS TO DISCOUNT ---
    // @Override
    // public double pointsToDiscount(int id, int amount, double price) {
    //     Customer customer = findById(id)
    //         .orElseThrow(() -> new InvalidSettersException("Customer with ID " + id + " not found."));

    //     if (amount <= 0 || amount > customer.getLoyaltyPoints()) {
    //             throw new InvalidSettersException("Not enough loyalty points or invalid amount.");
    //         }

    //     double resultPrice = price - (double) amount;

    //     int newLoyaltyPoints = customer.getLoyaltyPoints() - amount;
    //     customer.setLoyaltyPoints(newLoyaltyPoints);
    //     return resultPrice;
    // }

    @Override
    public double pointsToDiscount(int customerId, int amount, double price) {
        Customer customer = findById(customerId)
            .orElseThrow(() -> new InvalidSettersException("Customer with ID " + customerId + " not found."));
        
        if (amount <= 0 || amount > customer.loyaltyPoints()) {
                throw new InvalidSettersException("Not enough loyalty points or invalid amount.");
        }

        double resultPrice = price - (double) amount;
        int newLoyaltyPoints = customer.loyaltyPoints() - amount;
        
        Customer updatedCustomer = BaseCustomer.copyOf(customer)
            .withLoyaltyPoints(newLoyaltyPoints);

        storage.updateCustomer(customer, updatedCustomer);
        return resultPrice;
    }
}