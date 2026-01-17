package ui.menus;

import java.util.Scanner;

import interfaces.Menu; 

public class StoreMenu extends BaseMenu implements Menu {
    private final Menu productMenu; 
    private final Menu employeeMenu;

    public StoreMenu(Menu productMenu, Menu employeeMenu, Scanner scanner) {
        super(scanner);
        this.productMenu = productMenu;
        this.employeeMenu = employeeMenu;
    }

    @Override
    public void displayOptions() {
        System.out.println("\n=== GROCERY STORE SYSTEM ===");
        System.out.println("1. Product Management ->");
        System.out.println("2. Employee Management ->");
        System.out.println("3. Customer Management ->");
        System.out.println("0. Exit");
        System.out.print("Choice > ");
    }

    @Override
    public void run() {
        boolean exit = false;
        while (!exit) {
            displayOptions();
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1" -> productMenu.run(); // Proucts
                    case "2" -> employeeMenu.run(); // Employees
                    // case "3" -> customerMenu.run(); // Customers
                    case "0" -> exit = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}