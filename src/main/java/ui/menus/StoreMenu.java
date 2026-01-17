package ui.menus;

import java.util.Scanner;

import interfaces.Menu; 

public class StoreMenu extends BaseMenu implements Menu {
    private final Menu productMenu; 

    public StoreMenu(Menu productMenu, Scanner scanner) {
        super(scanner);
        this.productMenu = productMenu;
    }

    @Override
    public void displayOptions() {
        System.out.println("\n=== GROCERY STORE SYSTEM ===");
        System.out.println("1. Product Management");
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
                    case "1" -> productMenu.run(); 
                    case "0" -> exit = true;
                    default -> System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}