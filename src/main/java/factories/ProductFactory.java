package factories;

import java.math.BigDecimal;

import models.productModels.BaseFreshProduct;
import models.productModels.BaseFrozenProduct;
import models.productModels.Product;

public class ProductFactory {

    public static Product createFreshProduct(
            int productId, 
            String name, 
            BigDecimal unitPrice,
            double quantity,
            String category
        ) {
        return new BaseFreshProduct.Builder() 
                .productId(productId)
                .name(name)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .category(category)
                .build();
    }


    public static Product createFrozenProduct(
            int productId, 
            String name, 
            BigDecimal unitPrice, 
            double quantity,
            int storageTemp, 
            String category
        ) {
        return new BaseFrozenProduct.Builder()
                .productId(productId)
                .name(name)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .category(category)
                .storageTemp(storageTemp)
                .build();
    }
}