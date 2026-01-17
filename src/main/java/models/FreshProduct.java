package models;

import java.math.BigDecimal;

public class FreshProduct extends Product {
    private double weight;

    public FreshProduct(int productId, String name, BigDecimal unitPrice, String category, double weight) {
        super(productId, name, unitPrice, false, category);
        this.weight = weight;
    }

    @Override
    public String getSpecificDetails() {
        String details = weight + " kg";
        if (isBulk()) {
            details += " (BULK)";
        } else {
            details += " (NOT BULK)";
        }
        return details;
    }

    @Override
    public BigDecimal getTotalPrice() {
        return this.unitPrice.multiply(BigDecimal.valueOf(weight));
    }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { if(weight > 0) this.weight = weight; }

    // --- CALCULATE BULK ( > 5 kg )
    public boolean isBulk() {
        return weight > 5.0;
    }
}