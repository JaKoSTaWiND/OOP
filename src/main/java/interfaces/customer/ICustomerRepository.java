package interfaces.customer;

import java.util.List;
import java.util.Optional;

import models.customerModels.Customer;

public interface ICustomerRepository {
    List<Customer> getAllCustomers();
    Optional<Customer> findById(int customerId);
    Optional<Customer> findByName(String fullName);
    void save(Customer customer);
    void update(Customer customer);
    void delete(int customerId);
}
