package factories;

import java.math.BigDecimal;

import models.productModels.BaseFreshProduct;
import models.productModels.BaseFrozenProduct;
import models.productModels.FreshProduct;
import models.productModels.FrozenProduct;
import models.productModels.Product;

public class ProductFactory {

    /**
     * Factory method primarily used by the Repository to reconstruct a {@link FreshProduct} 
     * from database records, including its discount status.
     * <p>
     * This method leverages the {@code BaseFreshProduct.Builder} to instantiate a 
     * thread-safe, immutable product object with all persistent fields.
     * </p>
     *
     * @param productId    the unique database ID.
     * @param name         the name of the fresh product.
     * @param unitPrice    the current price per unit/kg.
     * @param quantity     the initial stock quantity.
     * @param category     the product category (e.g., "Fruits").
     * @param isDiscounted the persistence state of the discount flag.
     * @return a fully initialized {@link FreshProduct} instance.
     */
    public static Product createFreshProduct( // for Repository
            int productId, 
            String name, 
            BigDecimal unitPrice,
            double quantity,
            String category,
            boolean isDiscounted
        ) {
        return new BaseFreshProduct.Builder() 
                .productId(productId)
                .name(name)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .category(category)
                .isDiscounted(isDiscounted)
                .build();
    }

    /**
     * Overloaded factory method for creating a new {@link FreshProduct} instance 
     * (e.g., via the Service layer or UI) with a default discount status of {@code false}.
     * <p>
     * This is a convenience method that simplifies the creation of new products 
     * that haven't been processed for discounts yet.
     * </p>
     *
     * @param productId the ID (usually 0 for new unsaved entities).
     * @param name      the name of the product.
     * @param unitPrice the unit price.
     * @param quantity  the quantity to add.
     * @param category  the category.
     * @return a {@link FreshProduct} with {@code isDiscounted} set to {@code false}.
     */
    public static Product createFreshProduct( // for Service
            int productId, 
            String name, 
            BigDecimal unitPrice,
            double quantity,
            String category
        ) {
        return createFreshProduct(productId, name, unitPrice, quantity, category, false);
    }

    /**
     * Factory method used by the Repository to reconstruct a {@link FrozenProduct} 
     * from database records, including storage temperature and discount status.
     * <p>
     * This method utilizes the {@code BaseFrozenProduct.Builder} to create an 
     * immutable instance with specific frozen-goods attributes like {@code storageTemp}.
     * </p>
     *
     * @param productId    the unique database ID.
     * @param name         the name of the frozen product.
     * @param unitPrice    the current price per unit.
     * @param quantity     the initial stock quantity.
     * @param storageTemp  the required storage temperature in degrees Celsius.
     * @param category     the product category (e.g., "Frozen Vegetables").
     * @param isDiscounted the persistence state of the discount flag.
     * @return a fully initialized {@link FrozenProduct} instance.
     */
    public static Product createFrozenProduct( // for Repository
            int productId, 
            String name, 
            BigDecimal unitPrice, 
            double quantity,
            int storageTemp, 
            String category,
            boolean isDiscounted
        ) {
        return new BaseFrozenProduct.Builder()
                .productId(productId)
                .name(name)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .category(category)
                .storageTemp(storageTemp)
                .isDiscounted(isDiscounted)
                .build();
    }

    /**
     * Overloaded factory method for creating a new {@link FrozenProduct} instance 
     * with a default discount status of {@code false}.
     * <p>
     * Commonly used by the Service layer when adding a new frozen item to the system 
     * before any manual discount is applied.
     * </p>
     *
     * @param productId   the ID (use 0 for new unsaved entities).
     * @param name        the name of the product.
     * @param unitPrice   the unit price.
     * @param quantity    the quantity to add.
     * @param storageTemp the required storage temperature (°C).
     * @param category    the category.
     * @return a {@link FrozenProduct} with {@code isDiscounted} set to {@code false}.
     */
    public static Product createFrozenProduct( // for Service
            int productId, 
            String name, 
            BigDecimal unitPrice, 
            double quantity,
            int storageTemp, 
            String category
        ) {
        return createFrozenProduct(productId, name, unitPrice, quantity, storageTemp, category, false);
    }
}