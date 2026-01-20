package factories;

import java.math.BigDecimal;

import models.productModels.BaseFreshProduct;
import models.productModels.BaseFrozenProduct;
import models.productModels.Product;

public class ProductFactory {

    public static Product createFreshProduct(int productId, String name, BigDecimal unitPrice, String category, double weight) {
        return new BaseFreshProduct.Builder() 
                .productId(productId)
                .name(name)
                .unitPrice(unitPrice)
                .category(category)
                .weight(weight)
                .build();
    }


    public static Product createFrozenProduct(int productId, String name, BigDecimal unitPrice, int storageTemp, String category) {
        return new BaseFrozenProduct.Builder()
                .productId(productId)
                .name(name)
                .unitPrice(unitPrice)
                .category(category)
                .storageTemp(storageTemp)
                .build();
    }
}