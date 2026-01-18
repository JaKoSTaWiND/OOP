package storage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import factories.EmployeeFactory;
import factories.ProductFactory;
import models.Customer;
import models.employeeModels.Employee;
import models.productModels.Product;

public final class DataStorage {
    private final List<Product> products = new ArrayList<>();
    private final List<Employee> employees = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();

    public DataStorage() {
        initData();
    }

    public void initData() {
        // --- PRODUCTS ---        
        products.add(ProductFactory.createFresh(1, "Red Apples", new BigDecimal("3.20"), "Fruits", 1.5));
        products.add(ProductFactory.createFresh(2, "Beef Steak", new BigDecimal("15.90"), "Meat", 0.8));

        products.add(ProductFactory.createFrozen(3, "Pizza Quattro", new BigDecimal("6.50"), -18, "Frozen Food"));
        products.add(ProductFactory.createFrozen(4, "Frozen Berries", new BigDecimal("4.00"), -20, "Desserts"));


        // --- EMPLOYEES ---
        employees.add(EmployeeFactory.createManager(1, "Alex Johnson", new BigDecimal("50.00"), 5));
        
        employees.add(EmployeeFactory.createCashier(2, "Maria Garcia", new BigDecimal("18.50"), 1));
        employees.add(EmployeeFactory.createCashier(3, "Ivan Petrov", new BigDecimal("18.50"), 2));
        

        // --- CUSTOMERS (пока без фабрики, как ты и просил) ---
        customers.add(new Customer(1, "Alice Cooper", "87771112233", 150, true));
        customers.add(new Customer(2, "Bob Marley", "87015554433", 40, false));
        customers.add(new Customer(3, "Charlie Brown", "87479998877", 0, true));
    }

    public List<Product> getProducts() { return products; }
    public List<Employee> getEmployees() { return employees; }
    public List<Customer> getCustomers() { return customers; }

    public void addProduct(Product product) { this.products.add(product); }
    public void addEmployee(Employee employee) { this.employees.add(employee); }
    public void addCustomer(Customer customer) { this.customers.add(customer); }
}