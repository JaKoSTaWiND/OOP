package ui.menus;

import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;

import interfaces.Menu; 

public class StoreMenu extends BaseMenu implements Menu {
    private final Menu productMenu; 
    private final Menu employeeMenu;
    private final Menu customerMenu;

    public StoreMenu(Menu productMenu, Menu employeeMenu, Menu customerMenu, Scanner scanner) {
        super(scanner);
        this.productMenu = productMenu;
        this.employeeMenu = employeeMenu;
        this.customerMenu = customerMenu;
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
                    case "3" -> customerMenu.run(); // Customers
                    case "0" -> exit = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());
            }
        }
    }
}