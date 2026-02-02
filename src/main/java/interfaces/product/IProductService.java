package interfaces.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import models.productModels.Product;

public interface IProductService {
    List<Product> getAllProducts();
    Optional<Product> findById(int id);
    Optional<Product> findByName(String name);
    List<Product> findByNameLike(String name);
    List<Product> findByPriceRange(BigDecimal min, BigDecimal max);

    void addProduct(Product product);
    void updateProduct(Product product);
    void deleteProduct(int id);
    
    void applyDiscount(int productId, double percentage);
    void calculatePriceWithVAT(int productId, double vatRate);

    List<Product> getProductsByType(Class<? extends Product> type); // Get products by their child class
}