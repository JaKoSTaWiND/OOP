package models.productModels;

import java.math.BigDecimal;

import exceptions.InvalidSettersException;

public class FrozenProduct extends Product {
    private int storageTemp;

    public FrozenProduct(int productId, String name, BigDecimal unitPrice, int storageTemp, String category) {
        super(productId, name, unitPrice, false, category);
        setStorageTemp(storageTemp);
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

    @Override
    public String getTemp() { return this.storageTemp + " °C"; }

    public final void setStorageTemp(int storageTemp) {
        if (storageTemp < -273) { 
            throw new InvalidSettersException("Temperature is too low!");
        }
        this.storageTemp = storageTemp;
    }

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