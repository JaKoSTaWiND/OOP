package app;

import java.util.Scanner;
import java.util.concurrent.Callable;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;
import org.fusesource.jansi.AnsiConsole;

import models.Employee;
import models.Product;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import storage.DataStorage;
import ui.TableRenderer;

@Command(
    name = "oop-app", 
    mixinStandardHelpOptions = true, 
    version = "1.0"
)
public class Main implements Callable<Integer> {
    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        int exitCode = new CommandLine(new Main()).execute(args);
        AnsiConsole.systemUninstall();
        System.exit(exitCode);
    }

    @Override
    public Integer call() {

        DataStorage storage = new DataStorage();


        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {

            System.out.println(ansi().eraseScreen().cursor(1, 1));
            System.out.println(ansi().fg(Ansi.Color.CYAN).bold().a("=== GROCERY STORE SYSTEM ===").reset());
            System.out.println();
            System.out.println("""
                    1. Product methods
                    2. Employee methods
                    3. Customer methods
                    """);
            System.out.println(ansi().fg(Ansi.Color.RED).a("0. Exit"));
            System.out.println();
            System.out.println(ansi().fg(Ansi.Color.CYAN).bold().a("Enter choice: ").reset());

            String choice = scanner.nextLine();

            switch (choice) {   
                case "1" -> { // --- 1. PRODUCT METHODS ---
                    System.out.println(ansi().eraseScreen().cursor(1, 1));
                    System.out.println(ansi().fg(Ansi.Color.CYAN).bold().a("=== SELECT OPTION ===").reset());
                    System.out.println("""
                            1. View data (products)
                            2. Apply duscount
                            3. Calculate price with VAT (НДС)
                            """);

                    String subChoice = scanner.nextLine(); 

                    switch (subChoice) {
                            case "1" -> {
                                System.out.print(ansi().reset());
                                TableRenderer.printProductTable(storage.getProducts());
                            }
                            case "2" -> {

                                System.out.print("\nEnter Product ID to apply discount: ");
                                String idInput = scanner.nextLine();
                                int targetId = Integer.parseInt(idInput);

                                Product foundProduct = storage.getProducts().stream()
                                        .filter(p -> p.getId() == targetId)
                                        .findFirst()
                                        .orElse(null);

                                if (foundProduct != null) {
                                    System.out.print("Enter discount percentage (0.1 == 10%): ");
                                    double percent = Double.parseDouble(scanner.nextLine());
                                    
                                    foundProduct.applyDiscount(percent);
                                    
                                    System.out.println(ansi().fg(Ansi.Color.GREEN).a("Success! Discount applied.").reset());
                                } else {
                                    System.out.println(ansi().fg(Ansi.Color.RED).a("Product with ID " + targetId + " not found!").reset());
                                }
                            }
                            case "3" -> {
                                System.out.print("\nEnter Product ID to calculate VAT: ");
                                int id = Integer.parseInt(scanner.nextLine());

                                Product product = storage.getProducts().stream()
                                        .filter(p -> p.getId() == id)
                                        .findFirst()
                                        .orElse(null);

                                if (product != null) {
                                    System.out.print("Enter VAT rate (e.g. 0.12 for 12%): ");
                                    double vatRate = Double.parseDouble(scanner.nextLine());
                                    
                                    product.calculatePriceWithVAT(vatRate);
                                    
                                    System.out.println(ansi().fg(Ansi.Color.GREEN).a("Price updated with VAT!").reset());
                                } else {
                                    System.out.println(ansi().fg(Ansi.Color.RED).a("Product not found!").reset());
                                }
                            }
                        }
                        System.out.print(ansi().reset());
                        System.out.println(ansi().fg(Ansi.Color.GREEN).bold().a("Press Enter to return to main menu..."));
                        scanner.nextLine();
                }


                case "2" -> { // --- 2. EMPLOYEE METHODS ---
                    System.out.println(ansi().eraseScreen().cursor(1, 1));
                    System.out.println(ansi().fg(Ansi.Color.CYAN).bold().a("=== SELECT OPTION ===").reset());
                    System.out.println("""
                            1. View data (employees)
                            2. Calculate monthly salary
                            3. Calculate experience
                            """);

                    String subChoice = scanner.nextLine(); 

                    switch (subChoice) {
                        case "1" -> {
                            System.out.print(ansi().reset());
                            TableRenderer.printEmployeeTable(storage.getEmployees());
                        }
                        case "2" -> {
                            System.out.print("\nEnter Employee ID to calculate salary: ");
                            int id = Integer.parseInt(scanner.nextLine());

                            Employee emp = storage.getEmployees().stream()
                                    .filter(e -> e.getId() == id)
                                    .findFirst()
                                    .orElse(null);

                            if (emp != null) {
                                System.out.print("Enter bonus amount (e.g. 500.0): ");
                                double bonus = Double.parseDouble(scanner.nextLine());
                                System.out.print("Enter worked hours: ");
                                int hours = Integer.parseInt(scanner.nextLine());

                                emp.calculateMouthlySalary(bonus, hours);
                            } else {
                                System.out.println(ansi().fg(Ansi.Color.RED).a("Employee not found!").reset());
                            }
                        }

                        case "3" -> {
                            System.out.print("\nEnter Employee ID to check experience: ");
                            int id = Integer.parseInt(scanner.nextLine());

                            Employee emp = storage.getEmployees().stream()
                                    .filter(e -> e.getId() == id)
                                    .findFirst()
                                    .orElse(null);

                            if (emp != null) {
                                long years = emp.calculateExperience();
                                System.out.println(ansi().fg(Ansi.Color.GREEN).a("Experience: " + years + " years").reset());
                            } else {
                                System.out.println(ansi().fg(Ansi.Color.RED).a("Employee not found!").reset());
                            }
                        }
                    }
                    System.out.print(ansi().reset());
                    System.out.println(ansi().fg(Ansi.Color.GREEN).bold().a("Press Enter to return to main menu..."));
                    scanner.nextLine();
                }

                case "3" -> { // --- 3. CUSTOMERS METHODS ---
                    System.out.println(ansi().eraseScreen().cursor(1, 1));
                    System.out.println(ansi().fg(Ansi.Color.CYAN).bold().a("=== SELECT OPTION ===").reset());
                    System.out.println("""
                            1. View data (customers)
                            2. Apply duscount
                            3. Calculate price with VAT (НДС)
                            """);

                    String subChoice = scanner.nextLine(); 

                    switch (subChoice) {
                        case "1" -> {
                            System.out.print(ansi().reset());
                            TableRenderer.printCustomerTable(storage.getCustomers());
                        }
                        case "2" -> {
                            System.out.print("\nEnter Customer ID: ");
                            int id = Integer.parseInt(scanner.nextLine());

                            var customer = storage.getCustomers().stream()
                                    .filter(c -> c.getId() == id)
                                    .findFirst()
                                    .orElse(null);

                            if (customer != null) {
                                System.out.print("Enter purchase amount to calculate points (1 point per 100 USD): ");
                                double amount = Double.parseDouble(scanner.nextLine());
                                customer.addLoyaltyPoints(amount);
                                System.out.println(ansi().fg(Ansi.Color.GREEN).a("Points added successfully!").reset());
                            } else {
                                System.out.println(ansi().fg(Ansi.Color.RED).a("Customer not found!").reset());
                            }
                        }
                        case "3" -> {
                            System.out.print("\nEnter Customer ID: ");
                            int id = Integer.parseInt(scanner.nextLine());

                            var customer = storage.getCustomers().stream()
                                    .filter(c -> c.getId() == id)
                                    .findFirst()
                                    .orElse(null);

                            if (customer != null) {
                                System.out.print("Enter current bill price: ");
                                double price = Double.parseDouble(scanner.nextLine());
                                System.out.print("How many points to use? (1 point = 1 USD): ");
                                int points = Integer.parseInt(scanner.nextLine());

                                boolean success = customer.pointsToDiscount(points, price);
                                
                                if (success) {
                                    System.out.println(ansi().fg(Ansi.Color.GREEN).a("Discount applied! Remaining points: " + customer.getLoyaltyPoints()).reset());
                                } else {
                                    System.out.println(ansi().fg(Ansi.Color.RED).a("Operation failed.").reset());
                                }
                            } else {
                                System.out.println(ansi().fg(Ansi.Color.RED).a("Customer not found!").reset());
                            }
                        }
                    }
                    System.out.println("\nPress Enter to return to main menu...");
                    scanner.nextLine();
                }

                case "0" -> {
                    running = false;
                }
                

            }


        }
        return 0;
    }
}