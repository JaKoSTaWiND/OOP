package ui.menus.productMenus;

import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;
import org.springframework.stereotype.Component;

import exceptions.EmptyDataException;
import exceptions.InvalidInputException;
import factories.ProductFactory;
import interfaces.Menu;
import interfaces.product.IProductService;
import models.productModels.BaseFreshProduct;
import models.productModels.FreshProduct;
import models.productModels.Product;
import ui.TableRenderer;

@Component
public class FreshProductMenu extends AbstractProductMenu implements Menu {
    
    public FreshProductMenu(IProductService productService, Scanner scanner) {
        super(scanner, productService);
    }

    @Override
    public void displayOptions() {
        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("\n--- FRESH PRODUCT MANAGEMENT ---").reset());
        System.out.println("""
            1. List All Fresh Products
            2. Add New Fresh Product
            3. Delete Fresh Product by ->
            4. Update Fresh Product by ->

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
                    case "1" -> TableRenderer.printProductTable(productService.getProductsByType(FreshProduct.class)); // list all fresh products

                    case "2" -> handleAddProduct("FRESH", (name, price, quantity, category) -> 
                        ProductFactory.createFreshProduct(0, name, price, quantity, category)
                    ); // add new fresh product

                    case "3" -> handleDeleteProduct(productService, FreshProduct.class); // delete fresh product

                    case "4" -> { // update fresh product
                        System.out.println(ansi().bold().fgCyan().a("--- UPDATE FRESH PRODUCT ---").reset());

                        findProductForUpdate(productService, FreshProduct.class).ifPresentOrElse(fresh -> {
                            System.out.println(ansi().fgYellow().bold().a("Editing: ").reset().a(fresh.name()));
                            System.out.println("1. Name | 2. Price | 3. Qty | 4. Category");
                            String field = scanner.nextLine();

                            try {
                                Product updated = switch (field) {
                                    case "1" -> BaseFreshProduct.copyOf(fresh).withName(readString("New Name: "));
                                    case "2" -> BaseFreshProduct.copyOf(fresh).withUnitPrice(readBigDecimal("New Price: "));
                                    case "3" -> BaseFreshProduct.copyOf(fresh).withQuantity(readDouble("New Quantity: "));
                                    case "4" -> BaseFreshProduct.copyOf(fresh).withCategory(readString("New Category: "));
                                    default -> fresh;
                                };
                                
                                productService.updateProduct(updated);
                                System.out.println(ansi().fgGreen().a("Updated successfully").reset());
                            } catch (InvalidInputException e) {
                                System.out.println(ansi().fgRed().a(e.getMessage()).reset());
                            }
                        }, () -> System.out.println(ansi().fgRed().a("Fresh product not found!").reset()));
                    }
                    case "0" -> back = true;
                }
            }

            catch (EmptyDataException |InvalidInputException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());
            }
        }
    }
}