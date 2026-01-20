package models.productModels;

import java.math.BigDecimal;

import org.immutables.value.Value;

@Value.Immutable
public abstract class FreshProduct extends Product {

    public abstract double weight();

    // --- DERIVED ---
    @Value.Derived
    public boolean isBulk() {   // --- CALCULATE BULK ( > 5 kg )
        return weight() > 5.0;
    }

    // --- VALIDATION ---
    @Value.Check
    protected void checkWeight() {
        if (weight() <= 0) {
            throw new IllegalStateException("Weight must be positive");
        }
    }

    @Override
    public String getSpecificDetails() {
        StringBuilder details = new StringBuilder();
        details.append(weight()).append(" kg.");

        if (isBulk()) {
            details.append("(BULK)");
        } else {
            details.append("(NOT BULK)");
        }
        return details.toString();
    }

    @Override
    public BigDecimal getTotalPrice() {
        return normalizedPrice().multiply(BigDecimal.valueOf(weight()));
    }
}