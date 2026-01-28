package services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import exceptions.InvalidSettersException;
import interfaces.product.IProductRepository;
import interfaces.product.IProductService;
import models.productModels.BaseFreshProduct;
import models.productModels.BaseFrozenProduct;
import models.productModels.FreshProduct;
import models.productModels.FrozenProduct;
import models.productModels.Product;

@Service
public class ProductService implements IProductService {
    
    private final IProductRepository repository;

    public ProductService(IProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Product> findById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return repository.getAllProducts();
    }

    @Override
    public void addProduct(Product product) {
        if (repository.findById(product.productId()).isPresent()) {
            throw new InvalidSettersException("Product with ID " + product.productId() + " already exists.");
        }
        repository.save(product);
    }

    @Override
    @Transactional
    public void applyDiscount(int productId, double percentage) {
        Product product = findById(productId)
                .orElseThrow(() -> new InvalidSettersException("Product with ID " + productId + " not found."));
        
        if (percentage < 0 || percentage > 100) {
            throw new InvalidSettersException("Discount percentage must be between 0 and 100.");
        }

        BigDecimal discountFactor = new BigDecimal(String.valueOf(1.0 - percentage / 100.0));
        BigDecimal newPrice = product.unitPrice().multiply(discountFactor).setScale(2, RoundingMode.HALF_UP);

        Product updatedProduct = switch (product) {
            case FreshProduct fresh -> BaseFreshProduct.copyOf(fresh)
                    .withUnitPrice(newPrice)
                    .withIsDiscounted(true);
            case FrozenProduct frozen -> BaseFrozenProduct.copyOf(frozen)
                    .withUnitPrice(newPrice)
                    .withIsDiscounted(true);
            default -> throw new InvalidSettersException("Unknown product type");
        };

        repository.update(updatedProduct);
    }

    @Override
    @Transactional
    public void calculatePriceWithVAT(int productId, double vatRate) {
        Product product = findById(productId)
                .orElseThrow(() -> new InvalidSettersException("Product with ID " + productId + " not found."));
         
        BigDecimal vatFactor = new BigDecimal(String.valueOf(1.0 + vatRate));
        BigDecimal newPrice = product.unitPrice().multiply(vatFactor).setScale(2, RoundingMode.HALF_UP);

        Product updatedProduct = switch (product) {
            case FreshProduct fresh -> BaseFreshProduct.copyOf(fresh)
                    .withUnitPrice(newPrice);
            case FrozenProduct frozen -> BaseFrozenProduct.copyOf(frozen)
                    .withUnitPrice(newPrice);
            default -> throw new InvalidSettersException("Unknown product type");
        };

        repository.update(updatedProduct);
    }

    @Override
    public List<Product> getProductsByType(Class<? extends Product> type) {
        return repository.getAllProducts().stream()
                .filter(type::isInstance)
                .toList();
    }
}