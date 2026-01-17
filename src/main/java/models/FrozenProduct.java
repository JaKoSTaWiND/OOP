package models;

import java.math.BigDecimal;

public class FrozenProduct extends Product {
    private int storageTemp;

    public FrozenProduct(int productId, String name, BigDecimal unitPrice, int storageTemp, String category) {
        super(productId, name, unitPrice, false, category);
        this.storageTemp = storageTemp;
    }

    @Override
    public String getSpecificDetails() {
        String details = storageTemp + "°C";
        if (isDeepFreeze()) {
            details += " (DEEP)";
        } else {
            details += " (NOT DEEP)";
        }
        return details;
    }

    @Override
    public BigDecimal getTotalPrice() {
        return this.unitPrice;
    }

    public int getStorageTemp() { return storageTemp; }
    public void setStorageTemp(int temp) { this.storageTemp = temp; }

    // --- DEEP FREEZE ---
    public boolean isDeepFreeze() {
        return storageTemp <= -18;
    }

    // --- ADVICE ---
    @Override
    public String getDefrostAdvice() {
        return "Keep at room temperature for 2 hours before cooking.";
    }
}