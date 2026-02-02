package interfaces.customer;

import java.util.List;
import java.util.Optional;

import models.customerModels.Customer;

public interface ICustomerService {
    List <Customer> getAllCustomers();
    Optional<Customer> findById(int id);
    Optional<Customer> findByName(String fullName);

    void addCustomer(Customer customer);
    void updateCustomer(Customer customer);
    void deleteCustomer(int customerId);

    void addLoyaltyPoints(int id, int amount);
    double pointsToDiscount(int id, int amount, double price);
}
