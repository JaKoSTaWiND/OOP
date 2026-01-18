package factories;

import java.math.BigDecimal;

import models.productModels.FreshProduct;
import models.productModels.FrozenProduct;
import models.productModels.Product;

public class ProductFactory {

    public static Product createFresh(int id, String name, BigDecimal price, String category, double weight) {
        return new FreshProduct(id, name, price, category, weight);
    }

    public static Product createFrozen(int id, String name, BigDecimal price, int temp, String category) {
        return new FrozenProduct(id, name, price, temp, category);
    }
}