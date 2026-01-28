package interfaces.product;

import java.util.List;
import java.util.Optional;

import models.productModels.Product;

public interface IProductRepository {
    List<Product> getAllProducts();
    Optional<Product> findById(int id);
    void save(Product product);
    void update(Product product);
    void delete(int id);
}
