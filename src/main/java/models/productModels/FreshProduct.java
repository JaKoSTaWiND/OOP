package models.productModels;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.immutables.value.Value;

import models.ImmutableStyle;

@ImmutableStyle
@Value.Immutable
public abstract class FreshProduct extends Product {

    // --- DERIVED ---
    @Value.Derived
    public boolean isBulk() {   // --- CALCULATE BULK ( > 5 kg )
        return quantity() > 5.0;
    }

    @Override
    public String getSpecificDetails() {
        StringBuilder details = new StringBuilder();
        details.append(quantity()).append(" kg.");

        if (isBulk()) {
            details.append("(BULK)");
        } else {
            details.append("(NOT BULK)");
        }
        return details.toString();
    }

    @Override
    public BigDecimal getTotalPrice() {
        return normalizedPrice().multiply(BigDecimal.valueOf(quantity())).setScale(2, RoundingMode.HALF_UP);
    }
}