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
import models.productModels.BaseFreshProduct;
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

                    case "2" -> { // add new fresh product
                        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("--- ADD NEW FRESH PRODUCT ---").reset());
                        String name = readString("Enter Name: ");
                        BigDecimal price = readBigDecimal("Enter Price Per KG: ");
                        double quantity = readDouble("Enter Quantity (KG): ");
                        String category = readString("Enter Category: ");

                        Product product = ProductFactory.createFreshProduct(0, name, price, quantity, category);
                        productService.addProduct(product);
                        System.out.println(ansi().fg(Ansi.Color.GREEN).a("Fresh Product added to database").reset());
                    }

                    case "3" -> { // delete fresh product
                        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("--- DELETE FRESH PRODUCT ---").reset());
                        System.out.println("""
                        1. By ID
                        2. By Name
                        """);
                        String deleteChoice = scanner.nextLine();

                        int idToDelete = -1;

                        if (deleteChoice.equals("1")) {
                            idToDelete = readInt("Enter ID to delete: ");
                        } else if (deleteChoice.equals("2")) {
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

                    case "4" -> { // update fresh product
                        System.out.println(ansi().bold().fg(Ansi.Color.CYAN).a("--- UPDATE FRESH PRODUCT ---").reset());
                        System.out.println("""
                        Find by:
                        1. ID
                        2. Name
                        """);
                        String updateChoice = scanner.nextLine();

                        Optional<Product> found = (updateChoice.equals("1")) 
                            ? productService.findById(readInt("Enter ID: "))
                            : productService.findByName(readString("Enter Name: "));

                        if (found.isPresent() && found.get() instanceof FreshProduct fresh) {
                            System.out.println(ansi().fgYellow().bold().a("Editing: ").reset().a(fresh.name()));
                            System.out.println("""
                            1. Name
                            2. Price
                            3. Qty
                            4. Category
                            """);
                            String field = scanner.nextLine();

                            Product updated = switch (field) {
                                case "1" -> BaseFreshProduct.copyOf(fresh).withName(readString("New Name: "));
                                case "2" -> BaseFreshProduct.copyOf(fresh).withUnitPrice(readBigDecimal("New Price: "));
                                case "3" -> BaseFreshProduct.copyOf(fresh).withQuantity(readDouble("New Quantity: "));
                                case "4" -> BaseFreshProduct.copyOf(fresh).withCategory(readString("New Category: "));
                                default -> fresh;
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

            catch (EmptyDataException |InvalidInputException e) {
                System.out.println(ansi().fg(Ansi.Color.RED).a(e.getMessage()).reset());
            }
        }
    }
}