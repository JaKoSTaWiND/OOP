package factories;

import java.math.BigDecimal;

import models.productModels.BaseFreshProduct;
import models.productModels.BaseFrozenProduct;
import models.productModels.Product;

public class ProductFactory {

    // --- FRESH PRODUCTS ---
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

    public static Product createFreshProduct( // for Service
            int productId, 
            String name, 
            BigDecimal unitPrice,
            double quantity,
            String category
        ) {
        return createFreshProduct(productId, name, unitPrice, quantity, category, false);
    }


    // --- FROZEN PRODUCTS ---
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