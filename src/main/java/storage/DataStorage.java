package storage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import models.Customer;
import models.employeeModels.Cashier;
import models.employeeModels.Employee;
import models.employeeModels.Manager;
import models.productModels.FreshProduct;
import models.productModels.FrozenProduct;
import models.productModels.Product;
import models.productModels.SimpleProduct;

public final class DataStorage {
    private final List<Product> products = new ArrayList<>();
    private final List<Employee> employees = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();

    public DataStorage() {
        initData();
    }

    public void initData() {
        // --- PRODUCTS ---
        // SimpleProduct
        products.add(new SimpleProduct(1, "Olive Oil", new BigDecimal("12.50"), false, "Grocery"));
        products.add(new SimpleProduct(2, "Pasta Barilla", new BigDecimal("2.10"), true, "Grocery"));
        
        // FreshProduct
        products.add(new FreshProduct(3, "Red Apples", new BigDecimal("3.20"), "Fruits", 1.5));
        products.add(new FreshProduct(4, "Beef Steak", new BigDecimal("15.90"), "Meat", 0.8));

        // FrozenProduct
        products.add(new FrozenProduct(5, "Pizza Quattro", new BigDecimal("6.50"), -18, "Frozen Food"));
        products.add(new FrozenProduct(6, "Frozen Berries", new BigDecimal("4.00"), -20, "Desserts"));


        // --- EMPLOYEES ---
        // Manager
        employees.add(new Manager(1, "Alex Johnson", new BigDecimal("50.00"), LocalDate.of(2022, 3, 1), 5));
        
        // Cashier
        employees.add(new Cashier(2, "Maria Garcia", new BigDecimal("18.50"), LocalDate.of(2024, 1, 15), 1));
        employees.add(new Cashier(3, "Ivan Petrov", new BigDecimal("18.50"), LocalDate.of(2024, 6, 10), 2));
        

        // --- CUSTOMERS ---
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