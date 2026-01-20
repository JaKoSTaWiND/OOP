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
import models.productModels.FrozenProduct;
import models.productModels.Product;
import ui.TableRenderer;
import ui.menus.BaseMenu;

@Component
public class FrozenProductMenu extends BaseMenu implements Menu {
    private final IProductService productService;

    public FrozenProductMenu(IProductService productService, Scanner scanner) {
        super(scanner);
        this.productService = productService;
    }

    @Override
    public void displayOptions() {
        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("\n--- FROZEN PRODUCT MANAGEMENT ---").reset());
        System.out.println("""
            1. List All Frozen Products
            2. Add New Frozen Product

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
                    case "1" -> TableRenderer.printProductTable(productService.getProductsByType(FrozenProduct.class));
                    case "2" -> {
                        int id = readInt("Enter ID: ");
                        String name = readString("Enter name: ");
                        BigDecimal price = readBigDecimal("Enter price: ");
                        int storageTemp = readPositiveInt("Enter storage temperature (°C): ");
                        String category = readString("Enter category: ");

                        Product product = ProductFactory.createFrozenProduct(id, name, price, storageTemp, category);
                        productService.addProduct(product);
                        System.out.println(ansi().fg(Ansi.Color.GREEN).a("Frozen product added successfully").reset());
                    }
                    case "0" -> back = true;
                }
            } 
            catch (EmptyDataException | InvalidInputException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());
            }
        }
    }
}
