package models.productModels;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.immutables.value.Value;

import models.ImmutableStyle;

@ImmutableStyle
public abstract class Product {

    public abstract int productId();
    public abstract String name();
    public abstract double quantity();
    public abstract BigDecimal unitPrice();
    public abstract String category();

    // --- DEFAULTS ---
    @Value.Default
    public boolean isDiscounted() {
        return false;
    }

    // --- VALIDATION ---
    @Value.Check
    protected void validateName() { // validate name
        if (name().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty.");
        }
    }
    @Value.Check
    protected void validateQuantity() { // validate quantity
        if (quantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
    }
    @Value.Check
    protected void validateUnitPrice() { // validate unitPrice
        if (unitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Unit price cannot be negative.");
        }
    }
    @Value.Check
    protected void validateCategory() { // validate category
        if (category().trim().isEmpty()) {
            throw new IllegalArgumentException("Category cannot be empty.");
        }   
    }

    // --- DERIVED ---
    @Value.Derived
    public BigDecimal normalizedPrice() {
        return unitPrice().setScale(2, RoundingMode.HALF_UP);
    }

    // --- ABSTRACT METHODS ---
    public abstract String getSpecificDetails(); // FreshProduct -> weight, isBulk; FrozenProduct -> storageTemp, isDeepFreeze
    public abstract BigDecimal getTotalPrice(); // FreshProduct -> unitPrice * weight; FrozenProduct -> unitPrice
    public String getWeight() { return "-"; }
    public String getTemp() { return "-"; }

    public String getDefrostAdvice() {
    return "No defrosting needed.";
    }
}