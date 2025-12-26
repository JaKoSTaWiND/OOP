package app;

import java.math.BigDecimal;
import java.time.LocalDate;
import models.Customer;
import models.Employee;
import models.Product;

public class Main {

    static void main(String[] args) {
        System.out.println("Launched");

        // --- PRODUCT CLASS ---
        System.out.println("---  ---");
        System.out.println("--- Product class ---");

        Product product1 = new Product(
                1,
                "Product name",
                new BigDecimal("999.00"),
                false,
                "category1"
        );

        System.out.println("Created product" + product1);

        // --- METHOD1 ---
        double percentage = 0.15;
        product1.applyDiscount(percentage);

        // --- METHOD2 ---
        double vatRate = 0.22;
        product1.calculatePriceWithVAT(vatRate);



        // --- EMPLOYEE CLASS ---
        System.out.println("---  ---");
        System.out.println("--- Employee class ---");

        Employee employee1 = new Employee(
                1,
                "Employee name",
                new BigDecimal("45.90"),
                "position 1",
                true,
                LocalDate.of(2023, 12, 17)
        );

        System.out.println("Created employee" + employee1);

        // --- METHOD1 ---
        double bonusPercentage = 1200.50;
        int workedHours = 99;

        employee1.calculateMouthlySalary(bonusPercentage, workedHours);

        // --- METHOD2 ---
        employee1.calculateExperience();



        // --- CUSTOMER CLASS ---
        System.out.println("---  ---");
        System.out.println("--- Customer class ---");

        Customer customer1 = new Customer(
                1,
                "Customer name",
                "87777777777",
                500,
                false
        );

        System.out.println("Created customer" + customer1);

        // --- METHOD1 ---
        double amount = 50.00;

        customer1.addLoyaltyPoints(amount);

        // --- METHOD2 ( IF TRUE ) ---
        int pointsToUse = 30;
        double price = 1779.90;

        customer1.pointsToDiscount(pointsToUse, price);

        // --- METHOD2 ( IF FALSE ) ---
        int pointsToUse_false = 10000;

        customer1.pointsToDiscount(pointsToUse_false, price);




    }
}
