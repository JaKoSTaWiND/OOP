package models;

import java.math.BigDecimal;

public class FrozenProduct extends Product {
    private int storageTemp;

    public FrozenProduct(int productId, String name, BigDecimal unitPrice, String category, int storageTemp) {
        super(productId, name, unitPrice, false, category);
        this.storageTemp = storageTemp;
    }

    public int getStorageTemp() { return storageTemp; }
    public void setStorageTemp(int temp) { this.storageTemp = temp; }

    // --- DEEP FREEZE ---
    public boolean isDeepFreeze() {
        return storageTemp <= -18;
    }

    // --- ADVICE ---
    public String getDefrostAdvice() {
        return "Keep at room temperature for 2 hours before cooking.";
    }
}