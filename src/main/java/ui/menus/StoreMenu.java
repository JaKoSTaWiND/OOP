package ui.menus;

import java.util.Scanner;

import interfaces.Menu;
import services.productServices.FreshProductService;
import services.productServices.FrozenProductService;
import services.productServices.ProductService;
import storage.DataStorage;
import ui.menus.productMenus.ProductMenu;

public class StoreMenu implements Menu {

    private final Scanner scanner = new Scanner(System.in);

    private final ProductMenu productMenu;

    public StoreMenu(DataStorage storage) {

        // --- SERVICES ---
        // --- PRODUCT SERVICES ---
        ProductService productService = new ProductService(storage); // Product
        FreshProductService freshProductService = new FreshProductService(storage); // FreshProduct 
        FrozenProductService frozenProductService = new FrozenProductService(storage); // FrozenProduct

        // --- EMPLOYEE SERVICES ---

        this.productMenu = new ProductMenu(productService, freshProductService, frozenProductService, scanner);
    }


    @Override
    public void displayOptions() {
        System.out.println("=== MAIN MENU ===");
        System.out.println("1. Product Management");
        System.out.println("2. Employee Management");
        System.out.println("3. Customer Management");
        System.out.println("0. Exit");
        System.out.print("Choice > ");
    }

    @Override
    public void run() {
        boolean exit = false;
        while (!exit) {
            displayOptions();
            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> productMenu.run(); // Product menu
                case "0" -> exit = true;
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }
}
