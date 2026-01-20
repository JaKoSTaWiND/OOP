package ui.menus;

import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import interfaces.Menu; 

@Component
public class StoreMenu extends BaseMenu implements Menu {
    private final Menu simpleProductMenu; 
    private final Menu simpleEmployeeMenu;
    private final Menu simpleCustomerMenu;

    public StoreMenu(
            @Lazy Menu simpleProductMenu,
            @Lazy Menu simpleEmployeeMenu,
            @Lazy Menu simpleCustomerMenu,
            Scanner scanner
        ) {
        super(scanner);
        this.simpleProductMenu = simpleProductMenu;
        this.simpleEmployeeMenu = simpleEmployeeMenu;
        this.simpleCustomerMenu = simpleCustomerMenu;
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
                    case "1" -> simpleProductMenu.run(); // Products
                    case "2" -> simpleEmployeeMenu.run(); // Employees
                    case "3" -> simpleCustomerMenu.run(); // Customers
                    case "0" -> exit = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());
            }
        }
    }
}