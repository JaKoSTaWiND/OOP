package ui.menus.productMenus;

import java.math.BigDecimal;
import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import static org.fusesource.jansi.Ansi.ansi;
import org.springframework.stereotype.Component;

import exceptions.EmptyDataException;
import exceptions.InvalidInputException;
import factories.ProductFactory;
import interfaces.IProductService;
import interfaces.Menu;
import models.productModels.FreshProduct;
import models.productModels.Product;
import ui.TableRenderer;
import ui.menus.BaseMenu;

@Component
public class FreshProductMenu extends BaseMenu implements Menu {
    private final IProductService productService;


    public FreshProductMenu(IProductService productService, Scanner scanner) {
        super(scanner);
        this.productService = productService;
    }

    @Override
    public void displayOptions() {
        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("\n--- FRESH PRODUCT MANAGEMENT ---").reset());
        System.out.println("""
            1. List All Fresh Products
            2. Add New Fresh Product

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
                    case "1" -> TableRenderer.printProductTable(productService.getProductsByType(FreshProduct.class));
                    case "2" -> {
                        int id = readInt("Enter ID: ");
                        String name = readString("Enter Name: ");
                        BigDecimal price = readBigDecimal("Enter Price Per KG: ");
                        double quantity = readDouble("Enter Quantity (KG): ");
                        String category = readString("Enter Category: ");

                        Product product = ProductFactory.createFreshProduct(id, name, price, quantity, category);
                        productService.addProduct(product);
                        System.out.println(ansi().fg(Ansi.Color.GREEN).a("Fresh product added successfully").reset());

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