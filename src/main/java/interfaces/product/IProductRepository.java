package interfaces.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import models.productModels.Product;

public interface IProductRepository {
    List<Product> getAllProducts();
    Optional<Product> findById(int id);
    Optional<Product> findByName(String name);
    List<Product> findByNameLike(String name);
    List<Product> findByPriceRange(BigDecimal min, BigDecimal max);
    void save(Product product);
    void update(Product product);
    void delete(int id);


}