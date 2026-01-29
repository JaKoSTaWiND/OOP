package ui.menus.productMenus;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

import exceptions.InvalidInputException;
import interfaces.product.IProductService;
import models.productModels.Product;
import ui.menus.BaseMenu;

public abstract class AbstractProductMenu extends BaseMenu {
    protected final IProductService productService;

    public AbstractProductMenu(Scanner scanner, IProductService productService) {
        super(scanner);
        this.productService = productService;
    }

    /**
     * Provides a standardized workflow for adding new products to the system.
     * 
     * <p>
     * This template method handles the repetitive UI tasks of collecting common 
     * product attributes (name, price, quantity, and category). It then delegates 
     * the actual instantiation to the {@link ProductCreator} functional interface, 
     * allowing subclasses to inject specific logic or additional fields 
     * (e.g., storage temperature for frozen goods).
     * </p>
     * 
     * @param typeName the display name of the product category (e.g., "FRESH" or "FROZEN").
     * @param creator  a functional callback that handles the final product creation.
     * @see ProductCreator
     */
    protected void handleAddProduct(String typeName, ProductCreator creator) throws InvalidInputException {
        System.out.println(ansi().bold().fgCyan().a("--- ADD NEW " + typeName + " PRODUCT ---").reset());
        
        String name = readString("Enter Name: ");
        BigDecimal price = readBigDecimal("Enter Price: ");
        double quantity = readDouble("Enter Quantity: ");
        String category = readString("Enter Category: ");

        // Вызываем переданную логику создания
        Product product = creator.createProduct(name, price, quantity, category);
        
        productService.addProduct(product);
        System.out.println(ansi().fgGreen().a(typeName + " Product added to database").reset());
    }

    /**
     * Functional interface for product instantiation.
     * 
     * Used by {@link #handleAddProduct(String, ProductCreator)} to delegate 
     * the call to specific factory methods after common data has been gathered.
     */
    @FunctionalInterface
    public interface ProductCreator {
        /**
         * Creates a concrete Product instance.
         *
         * @param name     the gathered product name.
         * @param price    the gathered unit price.
         * @param qty      the gathered quantity.
         * @param category the gathered category string.
         * @return a concrete implementation of {@link Product}.
         * @throws InvalidInputException if additional data gathered within the creator is invalid.
         */
        Product createProduct(String name, BigDecimal price, double qty, String category) throws InvalidInputException;
    }


    /**
     * Executes a generic product deletion flow with type safety.
     * 
     * <p>
     * The method allows searching for a product by ID or Name, but strictly filters 
     * the result using {@code allowedType.isInstance()}. This prevents cross-type 
     * deletions (e.g., ensuring {@code FreshProductMenu} cannot delete a {@code FrozenProduct}).
     * </p>
     * 
     * @param productService the service used for database operations.
     * @param allowedType    the specific class of {@link Product} permitted for deletion 
     * in the current menu context.
     */
    protected void handleDeleteProduct(IProductService productService, Class<? extends Product> allowedType) throws InvalidInputException {
        System.out.println(ansi().bold().fgCyan().a("\n--- DELETE PRODUCT ---").reset());
        System.out.println("""
        1. By ID
        2. By Name
        """);
        String choice = scanner.nextLine();

        int idToDelete = -1;

        if (choice.equals("1")) {
            int id = readInt("Enter ID to delete: ");
            idToDelete = productService.findById(id) // search by ID
                    .filter(allowedType::isInstance)
                    .map(Product::productId)
                    .orElse(-1);
        } else if (choice.equals("2")) {
            String name = readString("Enter Name to delete: ");
            idToDelete = productService.findByName(name) // search by Name
                    .filter(allowedType::isInstance)
                    .map(Product::productId)
                    .orElse(-1);
        }

        if (idToDelete != -1) {
            productService.deleteProduct(idToDelete);
            System.out.println(ansi().fgGreen().a("Deleted successfully").reset());
        } else {
            System.out.println(ansi().fgRed().a("Product not found in this category.").reset());
        }
    }

    /**
     * Generic product search logic for the update flow.
     * Ensures the product exists and matches the expected type.
     * @param productService the service used for database operations.
     * @param allowedType the specific class of {@link Product} permitted for update in the current menu context.
     * @return an {@link Optional} containing the found product of the correct type, or empty if not found or type mismatch.
     */
    protected <T extends Product> Optional<T> findProductForUpdate(IProductService productService, Class<T> allowedType) throws InvalidInputException {
        System.out.println("Find by: 1. ID | 2. Name");
        String choice = scanner.nextLine();

        Optional<Product> found = (choice.equals("1")) 
            ? productService.findById(readInt("Enter ID: ")) 
            : productService.findByName(readString("Enter Name: "));

        return found.filter(allowedType::isInstance).map(allowedType::cast);
    }
}
