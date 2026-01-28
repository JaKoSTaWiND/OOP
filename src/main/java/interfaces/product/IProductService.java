package interfaces.product;

import java.util.List;
import java.util.Optional;

import models.productModels.Product;

public interface IProductService {
    void addProduct(Product product);
    List<Product> getAllProducts();
    Optional<Product> findById(int id);
    
    void applyDiscount(int productId, double percentage);
    void calculatePriceWithVAT(int productId, double vatRate);

    List<Product> getProductsByType(Class<? extends Product> type); // Get products by their child class
}