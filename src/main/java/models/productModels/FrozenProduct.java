package models.productModels;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.immutables.value.Value;

import models.ImmutableStyle;

@ImmutableStyle
@Value.Immutable
public abstract class FrozenProduct extends Product {

    public abstract int storageTemp();

    // --- DERIVED ---
    @Value.Derived
    public boolean isDeepFreeze() {   // --- DEEP FREEZE IF <= -18C
        return storageTemp() <= -18;
    }

    // --- VALIDATION ---
    @Value.Check
    protected void checkStorageTemp() {
        if (storageTemp() < -273) { 
            throw new IllegalStateException("Temperature is too low!");
        }
    }

    @Override
    public String getSpecificDetails() {
        StringBuilder details = new StringBuilder();

        if (isDeepFreeze()) {
            details.append(" (DEEP)");
        } else {
            details.append(" (NOT DEEP)");
        }
        return details.toString();
    }

    @Override
    public BigDecimal getTotalPrice() {
        return unitPrice().multiply(BigDecimal.valueOf(quantity())).setScale(2, RoundingMode.HALF_UP);
    }
    
    @Override
    public String getDefrostAdvice() {
        return "Keep at room temperature for 2 hours before cooking.";
    }
}