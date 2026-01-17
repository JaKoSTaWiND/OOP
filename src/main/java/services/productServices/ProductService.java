package services.productServices;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import models.Product;
import storage.DataStorage;

public class ProductService {
    private final DataStorage storage;

    public ProductService(DataStorage storage) {
        this.storage = storage;
    }

    // --- CHECK IF ID IS TAKEN ---
    private boolean isIdTaken(int id) {
        return storage.getProducts().stream().anyMatch(p -> p.getId() == id);
    }

    // --- FIND PRODUCT BY ID ---
    public Optional<Product> findProductById(int productId) {
        return storage.getProducts().stream()
                .filter(product -> product.getId() == productId)
                .findFirst();
    }

    // --- GET ALL PRODUCTS ---
    public List<Product> getAllProducts() {
        return storage.getProducts();
    }

    // --- ADD PRODUCT ---
    public void addProduct(int productId, String name, BigDecimal unitPrice, boolean isDiscounted, String category) {
        if (!isIdTaken(productId)) { 
            Product newProduct = new Product(productId, name, unitPrice, isDiscounted, category);
            storage.addProduct(newProduct);
            System.out.println("Product '" + name + "' added successfully.");
        } else {
            System.out.println("Product with ID " + productId + " already exists.");
        }
    }


    public void applyDiscount(int productId, double percenatge) {
        findProductById(productId).ifPresent(product -> product.applyDiscount(percenatge));
    }

    public void calculatePriceWithVAT(int productId, double vatRate) {
        findProductById(productId).ifPresent(product -> product.calculatePriceWithVAT(vatRate));
    }
}