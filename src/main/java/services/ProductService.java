package services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import exceptions.InvalidSettersException;
import interfaces.IProductService;
import models.productModels.BaseFreshProduct;
import models.productModels.BaseFrozenProduct;
import models.productModels.FreshProduct;
import models.productModels.FrozenProduct;
import models.productModels.Product;
import storage.DataStorage;

@Service
public class ProductService implements IProductService {
    private final DataStorage storage;

    public ProductService(DataStorage storage) {
        this.storage = storage;
    }

    // --- FIND PRODUCT BY ID ---
    @Override
    public Optional<Product> findById(int id) {
        return storage.getProducts().stream()
                .filter(p -> p.productId() == id)
                .findFirst();
    }

    // --- GET ALL PRODUCTS ---
    @Override
    public List<Product> getAllProducts() {
        return storage.getProducts();
    }

    // --- ADD PRODUCT ---
    @Override
    public void addProduct(Product product) {
        if (findById(product.productId()).isPresent()) {
            throw new InvalidSettersException("Product with ID " + product.productId() + " already exists.");
            }
        storage.addProduct(product);
        }

    // --- APPLY DISCOUNT ---
    @Override
    public void applyDiscount(int productId, double percentage) {
        Product product = findById(productId)
                .orElseThrow(() -> new InvalidSettersException("Product with ID " + productId + " not found."));
        
        if (percentage < 0 || percentage > 100) {
            throw new InvalidSettersException("Discount percentage must be between 0 and 100.");
        }

        BigDecimal discountFactor = new BigDecimal(String.valueOf(1.0 - percentage));
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

        storage.updateProduct(product, updatedProduct);
    }

    // --- CALCULATE PRICE WITH VAT ---
    @Override
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

        storage.updateProduct(product, updatedProduct);
    }

    // --- GET PRODUCTS BY TYPE ---
    @Override
    public List<Product> getProductsByType(Class<? extends Product> type) { // Get products by their child class
        return storage.getProducts().stream()
                .filter(type::isInstance)
                .toList();
    }
}