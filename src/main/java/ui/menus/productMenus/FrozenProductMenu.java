package ui.menus.productMenus;

import java.math.BigDecimal;
import java.util.Optional;
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

                    case "2" -> { // add new frozen product
                        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("--- ADD NEW FROZEN PRODUCT ---").reset());
                        
                        String name = readString("Enter name: ");
                        BigDecimal price = readBigDecimal("Enter price: ");
                        double quantity = readDouble("Enter quantity: ");
                        int storageTemp = readInt("Enter storage temperature (°C): ");
                        String category = readString("Enter category: ");

                        Product product = ProductFactory.createFrozenProduct(0, name, price, quantity, storageTemp, category);
                        
                        productService.addProduct(product);
                        System.out.println(ansi().fgGreen().bold().a("Frozen Product added to database").reset());
                    }
                    
                    case "3" -> { // delete frozen product
                        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("--- DELETE FROZEN PRODUCT ---").reset());
                        System.out.println("""
                        1. By ID
                        2. By Name
                        """);
                        String delChoice = scanner.nextLine();
                        
                        int idToDelete = -1;
                        
                        if (delChoice.equals("1")) {
                            idToDelete = readInt("Enter ID to delete: ");
                        } else if (delChoice.equals("2")) {
                            String nameToDelete = readString("Enter Name to delete: ");
                            idToDelete = productService.getAllProducts().stream()
                                    .filter(p -> p.name().equalsIgnoreCase(nameToDelete))
                                    .map(Product::productId)
                                    .findFirst().orElse(-1);
                        }

                        if (idToDelete != -1) {
                            productService.deleteProduct(idToDelete);
                            System.out.println(ansi().fgGreen().a("Deleted successfully").reset());
                        } else {
                            System.out.println(ansi().fgRed().a("Product not found.").reset());
                        }
                    }

                    case "4" -> { // update frozen product
                        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("--- UPDATE FROZEN PRODUCT ---").reset());
                        System.out.println("""
                        Find by:
                        1. ID
                        2. Name
                        """);
                        String searchType = scanner.nextLine();

                        Optional<Product> found = (searchType.equals("1")) 
                            ? productService.findById(readInt("Enter ID: ")) 
                            : productService.findByName(readString("Enter Name: "));

                        if (found.isPresent() && found.get() instanceof FrozenProduct frozen) {
                            System.out.println(ansi().fgYellow().bold().a("Editing: ").reset().a(frozen.name()));
                            System.out.println("""
                            1. Name
                            2. Price
                            3. Qty
                            4. Temp
                            5. Category
                            """);
                            String field = scanner.nextLine();

                            Product updated = switch (field) {
                                case "1" -> BaseFrozenProduct.copyOf(frozen).withName(readString("New Name: "));
                                case "2" -> BaseFrozenProduct.copyOf(frozen).withUnitPrice(readBigDecimal("New Price: "));
                                case "3" -> BaseFrozenProduct.copyOf(frozen).withQuantity(readDouble("New Quantity: "));
                                case "4" -> BaseFrozenProduct.copyOf(frozen).withStorageTemp(readInt("New Temp: "));
                                case "5" -> BaseFrozenProduct.copyOf(frozen).withCategory(readString("New Category: "));
                                default -> frozen;
                            };

                            productService.updateProduct(updated);
                            System.out.println(ansi().fgGreen().a("Updated successfully").reset());
                        } else {
                            System.out.println(ansi().fgRed().a("Product not found!").reset());
                        }
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
