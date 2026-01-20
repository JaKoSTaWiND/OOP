package interfaces;

import java.util.List;
import java.util.Optional;

import models.customerModels.Customer;

public interface ICustomerService {
    void addCustomer(Customer customer);
    List <Customer> getAllCustomers();
    Optional<Customer> findById(int id);

    void addLoyaltyPoints(int id, int amount);
    double pointsToDiscount(int id, int amount, double price);
}
