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
import models.productModels.BaseFrozenProduct;
import models.productModels.FrozenProduct;
import models.productModels.Product;
import ui.TableRenderer;

@Component
public class FrozenProductMenu extends AbstractProductMenu implements Menu {

    public FrozenProductMenu(IProductService productService, Scanner scanner) {
        super(scanner, productService);
    }

    @Override
    public void displayOptions() {
        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("\n--- FROZEN PRODUCT MANAGEMENT ---").reset());
        System.out.println("""
            1. List All Frozen Products
            2. Add New Frozen Product
            3. Delete Frozen Product by ->
            4. Update Frozen Product by ->

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
                    case "1" -> TableRenderer.printProductTable(productService.getProductsByType(FrozenProduct.class)); // list all frozen products

                    /**
                     * {@link AbstractProductMenu}
                     */
                    case "2" -> handleAddProduct("FROZEN", (name, price, qty, cat) -> {
                        int temp = readInt("Enter storage temperature (°C): "); // addition field for frozen product
                        return ProductFactory.createFrozenProduct(0, name, price, qty, temp, cat);
                    }); // add new frozen product

                    /**
                     * {@link AbstractProductMenu}
                     */
                    case "3" -> handleDeleteProduct(productService, FrozenProduct.class); // delete frozen product

                    case "4" -> { // update frozen product
                        System.out.println(ansi().bold().fgCyan().a("--- UPDATE FROZEN PRODUCT ---").reset());

                        /**
                         * {@link AbstractProductMenu}
                         */
                        findProductForUpdate(productService, FrozenProduct.class).ifPresentOrElse(frozen -> {
                            System.out.println(ansi().fgYellow().bold().a("Editing: ").reset().a(frozen.name()));
                            System.out.println("1. Name | 2. Price | 3. Qty | 4. Temp | 5. Category");
                            String field = scanner.nextLine();

                            try {
                                Product updated = switch (field) {
                                    case "1" -> BaseFrozenProduct.copyOf(frozen).withName(readString("New Name: "));
                                    case "2" -> BaseFrozenProduct.copyOf(frozen).withUnitPrice(readBigDecimal("New Price: "));
                                    case "3" -> BaseFrozenProduct.copyOf(frozen).withQuantity(readDouble("New Quantity: "));
                                    case "4" -> BaseFrozenProduct.copyOf(frozen).withStorageTemp(readInt("New Temp: "));
                                    case "5" -> BaseFrozenProduct.copyOf(frozen).withCategory(readString("New Category: "));
                                    default -> frozen;
                                };
                                
                                productService.updateProduct(updated);
                                System.out.println(ansi().fgGreen().a("Updated successfully!").reset());
                            } catch (InvalidInputException e) {
                                System.out.println(ansi().fgRed().a(e.getMessage()).reset());
                            }
                        }, () -> System.out.println(ansi().fgRed().a("Frozen product not found!").reset()));
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
