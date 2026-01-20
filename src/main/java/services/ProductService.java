package services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import exceptions.InvalidSettersException;
import interfaces.IProductService;
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
                .filter(p -> p.getId() == id)
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
        if (findById(product.getId()).isPresent()) {
            throw new InvalidSettersException("Product with ID " + product.getId() + " already exists.");
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
        BigDecimal newPrice = product.getUnitPrice().multiply(discountFactor);

        product.setUnitPrice(newPrice);
        product.setIsDiscounted(true);
    }

    // --- CALCULATE PRICE WITH VAT ---
    @Override
    public void calculatePriceWithVAT(int productId, double vatRate) {
        Product product = findById(productId)
                .orElseThrow(() -> new InvalidSettersException("Product with ID " + productId + " not found."));
         
        BigDecimal vatFactor = new BigDecimal(String.valueOf(1.0 + vatRate));
        BigDecimal newPrice = product.getUnitPrice().multiply(vatFactor);

        product.setUnitPrice(newPrice);
    }

    // --- GET PRODUCTS BY TYPE ---
    @Override
    public List<Product> getProductsByType(Class<? extends Product> type) { // Get products by their child class
        return storage.getProducts().stream()
                .filter(type::isInstance)
                .toList();
    }
}