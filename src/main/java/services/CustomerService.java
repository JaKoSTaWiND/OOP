package services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import exceptions.InvalidSettersException;
import interfaces.customer.ICustomerRepository;
import interfaces.customer.ICustomerService;
import models.customerModels.BaseCustomer;
import models.customerModels.Customer;

/**
 * Core implementation of the {@link ICustomerService} interface.
 * <p>
 * This service manages the business logic for customer relations, including 
 * profile management, loyalty program operations, and reward-to-discount conversions.
 * It serves as the intermediary between the UI layer and the {@link ICustomerRepository}.
 * </p>
 * Key features include:
 * <ul>
 * <li>Transactional customer profile persistence (Add/Update/Delete).</li>
 * <li>Loyalty program management with point accumulation logic.</li>
 * <li>Point-to-currency conversion for transaction discounts.</li>
 * <li>Input validation and existence checks to maintain data integrity.</li>
 * </ul>
 * 
 * @see ICustomerService
 * @see ICustomerRepository
 */
@Service
public class CustomerService implements ICustomerService {

    private final ICustomerRepository repository;

    public CustomerService(ICustomerRepository repository) {
        this.repository = repository;
    }

    /**
     * Finds a customer by their unique identifier.
     * 
     * @param customerId the unique ID of the customer.
     * @return an {@link Optional} containing the customer if found, or empty otherwise.
     */
    @Override
    public Optional<Customer> findById(int customerId) {
        return repository.findById(customerId);
    }

    /**
     * Retrieves a customer profile using their full name.
     * 
     * @param fullName the complete name of the customer to search for.
     * @return an {@link Optional} containing the customer, or empty if no match is found.
     */
    @Override
    public Optional<Customer> findByName(String fullName) {
        return repository.findByName(fullName);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return repository.getAllCustomers();
    }

    /**
     * Persists a new customer record in the system.
     * 
     * @param customer the customer entity to be added.
     */
    @Override
    @Transactional
    public void addCustomer(Customer customer) {
        if (repository.findById(customer.customerId()).isPresent()) {
            throw new InvalidSettersException("Customer with ID " + customer.customerId() + " already exists.");
        }
        repository.save(customer);
    }

    /**
     * Removes a customer from the system based on their ID.
     *
     * @param customerId the unique identifier of the customer to be removed.
     */
    @Override
    @Transactional
    public void deleteCustomer(int customerId) {
        if (repository.findById(customerId).isEmpty()) {
            throw new InvalidSettersException("Customer with ID " + customerId + " already exists.");
        }

        repository.delete(customerId);
    }

    /**
     * Updates the information of an existing customer.
     * 
     * @param customer the customer instance containing updated data.
     */
    @Override
    @Transactional
    public void updateCustomer(Customer customer) {
        if (customer == null || customer.customerId() <= 0) {
            throw new InvalidSettersException("Invalid customer data for update.");
        }

        if (repository.findById(customer.customerId()).isEmpty()) {
            throw new InvalidSettersException("Customer with ID " + customer.customerId() + " does not exist.");
        }

        repository.update(customer);
    }

    // @Override
    // public void addLoyaltyPoints(int customerId, int amount) {
    //     Customer customer = findById(customerId)
    //         .orElseThrow(() -> new InvalidSettersException("Customer with ID " + customerId + " not found."));

    //     if (amount < 0) {
    //         throw new InvalidSettersException("Amount to add cannot be negative.");
    //     }

    //     int newLoyaltyPoints = customer.loyaltyPoints() + amount;

    //     Customer updatedCustomer = BaseCustomer.copyOf(customer)
    //         .withLoyaltyPoints(newLoyaltyPoints);

    //     storage.updateCustomer(customer, updatedCustomer);
    // }

    /**
     * Increases the customer's loyalty balance by a specified amount.
     * <p>
     * This method retrieves the current customer state, creates an updated 
     * immutable copy with increased points, and persists the change.
     * </p>
     * 
     * @param customerId the ID of the customer receiving points.
     * @param amount     the number of points to add (must be non-negative).
     */
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

        repository.update(updatedCustomer);
    }

    /**
     * Converts loyalty points into a monetary discount for a purchase.
     * <p>
     * Deducts the specified points from the customer's balance and calculates 
     * the final price. This operation is treated as a balance update.
     * </p>
     *
     * @param customerId the ID of the customer using points.
     * @param amount     the number of points to redeem.
     * @param price      the original transaction price.
     * @return the adjusted price after the point-based discount is applied.
     */
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

        repository.update(updatedCustomer);
        return resultPrice;
    }
}