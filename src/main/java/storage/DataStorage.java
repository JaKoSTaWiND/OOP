package storage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import models.Customer;
import models.Employee;
import models.Product;

public final class DataStorage {
    private final List<Product> products = new ArrayList<>();
    private final List<Employee> employees = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();

    public DataStorage() {
        initData();
    }

    public void initData() {
        // --- PRODUCTS ---
        products.add(new Product(1, "Laptop1", new BigDecimal("2990.90"), false, "Laptops"));
        products.add(new Product(2, "Laptop2", new BigDecimal("1990.90"), true, "Laptops"));
        products.add(new Product(3, "Laptop3", new BigDecimal("2590.90"), false, "Laptops"));

        products.add(new Product(4, "Phone1", new BigDecimal("790.90"), true, "Phones"));
        products.add(new Product(5, "Phone2", new BigDecimal("990.90"), false, "Phones"));
        products.add(new Product(6, "Phone3", new BigDecimal("1290.90"), false, "Phones"));

        // --- EMPLOYEES ---
        employees.add(new Employee(1, "First Employee", new BigDecimal("15"), "Seller", true, LocalDate.of(2025, 12, 12)));
        employees.add(new Employee(2, "Second Employee", new BigDecimal("25"), "Seller", true, LocalDate.of(2023, 7, 15)));
        employees.add(new Employee(3, "Third Employee", new BigDecimal("20"), "Loader", true, LocalDate.of(2020, 1, 9)));
        employees.add(new Employee(4, "Fourth Employee", new BigDecimal("70"), "Administrator", true, LocalDate.of(2020, 1, 9)));

        // --- CUSTOMERS ---
        customers.add(new Customer(1, "First Customer", "87777777777", 1000, true));
        customers.add(new Customer(2, "Second Customer", "81111111111", 100, false));
        customers.add(new Customer(3, "Third Customer", "82222222222", 700, false));
        customers.add(new Customer(4, "Fourth Customer", "83333333333", 0, false));
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<Customer> getCustomers() {
        return customers;
    }

}