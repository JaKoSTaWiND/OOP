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

/**
 * Core implementation of the {@link IProductService} interface.
 * <p>
 * This service provides business logic for managing various types of products, 
 * including CRUD operations, tax calculations, and discount applications. 
 * It acts as a bridge between the UI menus and the {@link IProductRepository}.
 * </p>
 * Key features include:
 * <ul>
 * <li>Type-safe product filtering.</li>
 * <li>Transactional updates for price modifications (VAT and Discounts).</li>
 * <li>Validation logic to prevent duplicate IDs or invalid inputs.</li>
 * </ul>
 * </p>
 * 
 * @see IProductService
 * @see IProductRepository
 */
@Service
public class ProductService implements IProductService {
    
    private final IProductRepository repository;

    public ProductService(IProductRepository repository) {
        this.repository = repository;
    }

    /**
     * Finds a product by its unique identifier.
     * 
     * @param id the unique ID of the product.
     * @return an {@link Optional} containing the product if found, or empty otherwise.
     */
    @Override
    public Optional<Product> findById(int id) {
        return repository.findById(id);
    }

    /**
     * Finds a product by its name.
     * 
     * @param name the name of the product.
     * @return an {@link Optional} containing the product if found, or empty otherwise.
     */
    @Override
    public Optional<Product> findByName(String name) {
        if (name == null || name.isBlank()) {
            return Optional.empty();
        }
        return repository.findByName(name);
    }

    @Override
    public List<Product> getAllProducts() {
        return repository.getAllProducts();
    }

    /**
     * Searches for products by a partial name match, ignoring case.
     * 
     * @param name the fragment of the name to search for.
     * @return a list of products containing the fragment.
     */
    @Override
    public List<Product> findByNameLike(String name) {
        if (name == null || name.isBlank()) {
            return List.of(); // Если строка пустая, возвращаем пустой список без запроса к БД
        }
        return repository.findByNameLike(name);
    }

    /**
     * Filters products within a specified price range.
     * 
     * @param min the minimum unit price.
     * @param max the maximum unit price.
     * @return a list of products within the [min, max] range.
     */
    @Override
    public List<Product> findByPriceRange(BigDecimal min, BigDecimal max) {
        if (min == null || max == null) {
            throw new InvalidSettersException("Price boundaries cannot be null.");
        }
        
        if (min.compareTo(max) > 0) {
            throw new InvalidSettersException("Minimum price (" + min + ") cannot be greater than maximum price (" + max + ").");
        }
        
        return repository.findByPriceRange(min, max);
    }

    /**
     * Adds a new product to the system.
     * 
     * @param product the product entity to persist.
     */
    @Override
    public void addProduct(Product product) {
        if (repository.findById(product.productId()).isPresent()) {
            throw new InvalidSettersException("Product with ID " + product.productId() + " already exists.");
        }
        repository.save(product);
    }

    /**
     * Removes an product record from the system.
     * 
     * @param id the ID of the product to dismiss.
     */
    @Override
    public void deleteProduct(int id) {
        if (repository.findById(id).isEmpty()) {
            throw new InvalidSettersException("Product with ID " + id + " not found.");
        }
        repository.delete(id);
    }

    /**
     * Updates an existing product's information.
     * 
     * @param product the product instance with modified data.
     */
    @Override
    @Transactional
    public void updateProduct(Product product) {
        if (product.productId() <= 0) {
            throw new InvalidSettersException("Invalid product ID for update.");
        }

        if (repository.findById(product.productId()).isEmpty()) {
            throw new InvalidSettersException("Product with ID " + product.productId() + " does not exist.");
        }

        repository.update(product);
    }

    /**
     * Applies a percentage discount to a product's unit price.
     * <p>
     * This method creates a copy of the existing product with the adjusted price 
     * and sets the {@code isDiscounted} flag to true. The calculation uses 
     * {@link RoundingMode#HALF_UP} for precision.
     * </p>
     * 
     * @param productId  the ID of the product to modify.
     * @param percentage the discount percentage (e.g., 10.0 for 10%).
     */
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

    /**
     * Adjusts the product price by adding Value Added Tax (VAT).
     * 
     * @param productId the ID of the product to modify.
     * @param vatRate   the tax rate as a decimal (e.g., 0.2 for 20% VAT).
     */
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

    /**
     * Filters all existing products by their specific class type.
     * 
     * @param type the class type to filter by (e.g., {@code FreshProduct.class}).
     * @return a list of products that are instances of the specified type.
     */
    @Override
    public List<Product> getProductsByType(Class<? extends Product> type) {
        return repository.getAllProducts().stream()
                .filter(type::isInstance)
                .toList();
    }
}