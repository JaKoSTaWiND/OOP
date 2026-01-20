package models.productModels;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.immutables.value.Value;

import exceptions.InvalidSettersException;

public abstract class Product {
    // protected int productId;
    // protected String name;
    // protected BigDecimal unitPrice;
    // protected boolean isDiscounted;
    // protected String category;

    public abstract int productId();
    public abstract String name();
    public abstract BigDecimal unitPrice();
    public abstract String category();

    // --- DEFAULTS ---
    @Value.Default
    public boolean isDiscounted() {
        return false;
    }

    // --- VALIDATION ---
    @Value.Check
    protected void valildate() {
        if (name().trim().isEmpty()) {
            throw new InvalidSettersException("Product name cannot be empty.");
        }
        if (unitPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidSettersException("Unit price cannot be negative.");
        }
        if (category().trim().isEmpty()) {
            throw new InvalidSettersException("Category cannot be empty.");
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

