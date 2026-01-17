package ui.menus.productMenus;

import java.math.BigDecimal;
import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;

import exceptions.InvalidInputException;
import interfaces.Menu;
import services.productServices.FreshProductService;
import services.productServices.FrozenProductService;
import services.productServices.ProductService;
import ui.TableRenderer;
import ui.menus.BaseMenu;

public class ProductMenu extends BaseMenu implements Menu {
    
    private final ProductService productService;
    private final FreshProductService freshProductService;
    private final FrozenProductService frozenProductService;

    private final FreshProductMenu freshProductMenu;
    private final FrozenProductMenu frozenProductMenu;

    public ProductMenu(ProductService productService, FreshProductService freshProductService, FrozenProductService frozenProductService, Scanner scanner) {
        super(scanner);
        this.productService = productService;
        this.freshProductService = freshProductService;
        this.frozenProductService = frozenProductService;
        this.frozenProductMenu = new FrozenProductMenu(frozenProductService, scanner);
        this.freshProductMenu = new FreshProductMenu(freshProductService, scanner);
    }

    @Override
    public void displayOptions() {
        System.out.println(ansi().fg(Ansi.Color.CYAN).bold().a("\n--- PRODUCT MANAGEMENT ---").reset());
        System.out.println("""
            1. List All Products
            2. Add New Product
            3. Apply Discount
            4. Calculate Price with VAT

            8. Fresh Product Management

            0. Back to Main Menu
                            """);
        System.out.print("Choice > ");
    }

    @Override
    public void run() {
        boolean back = false;
        while (!back) {
            displayOptions();
            String choice = scanner.nextLine();

            try {

                switch (choice) {
                    case "1" -> { // List all products
                        System.out.print(ansi().reset());
                        TableRenderer.printProductTable(productService.getAllProducts());
                    }
                    case "2" -> { // Add new product
                        System.out.println(ansi().fg(Ansi.Color.YELLOW).a("--- Add New Product ---").reset());
                        int id = readInt("Enter Product ID: "); 
                        String name = readString("Enter name");
                        BigDecimal price = readBigDecimal("Enter Price: ");
                        String cat = readString("Enter Category: ");
                        productService.addProduct(id, name, price, false, cat);
                        System.out.println(ansi().fg(Ansi.Color.GREEN).a("Product added!").reset());
                    }
                    case "3" -> { // Apply discount
                        int id = readInt("Enter Product ID for discount: ");
                        double discount = readDouble("Enter Discount Percentage: ");
                        productService.applyDiscount(id, discount);
                        System.out.println(ansi().fg(Ansi.Color.GREEN).a("Discount applied!").reset());
                    }
                    case "4" -> { // Calculate price with VAT
                        int id = readInt("Enter Product ID for VAT calculation: ");
                        double vatRate = readDouble("Enter VAT Rate (e.g., 0.2 for 20%): ");
                        productService.calculatePriceWithVAT(id, vatRate);
                        System.out.println(ansi().fg(Ansi.Color.GREEN).a("Price with VAT calculated!").reset());
                    }
                    case "8" -> freshProductMenu.run(); // FreshProduct Management
                    case "9" -> frozenProductMenu.run(); // FrozenProduct Management
                    case "0" -> back = true;
                    default -> System.out.println("Invalid option!");
                }
            }

            catch (exceptions.EmptyDataException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());
            }
            catch (InvalidInputException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());
                System.out.println(ansi().fg(Ansi.Color.RED).a("Invalid number format! Please enter digits only.").reset());
            }
        }
    }
}