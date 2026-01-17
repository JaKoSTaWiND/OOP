package models.productModels;

import java.math.BigDecimal;

public class SimpleProduct extends Product {

    public SimpleProduct(int productId, String name, BigDecimal unitPrice, boolean isDiscounted, String category) {
        super(productId, name, unitPrice, isDiscounted, category);
    }

    @Override
    public BigDecimal getTotalPrice() { // return unitPrice
        return this.unitPrice;
    }

    @Override
    public String getWeight() { return "-"; }
    
    @Override
    public String getTemp() { return "-"; }

    @Override
    public String getSpecificDetails() {
        return "N/A";
    }
}