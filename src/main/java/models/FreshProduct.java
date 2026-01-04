package models;

import java.math.BigDecimal;

public class FreshProduct extends Product {
    private double weight;

    public FreshProduct(int productId, String name, BigDecimal unitPrice, String category, double weight) {
        super(productId, name, unitPrice, false, category);
        this.weight = weight;
    }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { if(weight > 0) this.weight = weight; }

    // --- CALCULATE BULK ( > 5 kg )
    public boolean isBulk() {
        return weight > 5.0;
    }

    // --- WEIGHT TO PRICE ---
    public BigDecimal calculateTotalWeightPrice() {
        return getUnitPrice().multiply(new BigDecimal(String.valueOf(weight)));
    }
}