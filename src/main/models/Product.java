package main.models;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class Product {
    private int productId;
    private String name;
    private BigDecimal unitPrice;
    private boolean isDiscounted;
    private String category;

    // --- CONSTRUCTION ---
    public Product(int productId, String name, BigDecimal unitPrice, boolean isDiscounted, String category) {
        this.productId = productId;
        this.name = name;
        this.unitPrice = unitPrice.setScale(2, RoundingMode.HALF_UP);
        this.category = category;
        this.isDiscounted = isDiscounted;
    }

    // --- DEFAULT CONSTRUCTOR ---
    public Product(int i, String product1, BigDecimal bigDecimal) {
        this.isDiscounted = false;
    }


    // --- GETTERS  ---
    public BigDecimal getUnitPrice() { return unitPrice; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public boolean isDiscounted() { return  isDiscounted; }

    // --- SETTERS ---
    public void setName(String name) {
        if (name != null) {
            this.name = name;
        } else {
            System.out.println("Invalid name");
        }
    }

    public void setCategory(String category) {
        if (category != null) {
            this.name = category;
        } else {
            System.out.println("Invalid category");
        }
    }

    public void setUnitPrice(BigDecimal unitPrice) {

        if (unitPrice.compareTo(BigDecimal.ZERO) >= 0) {
            this.unitPrice = unitPrice;
        } else {
            System.out.println("Invalid unit price");
        }
    }

    public void setIsDiscounted(boolean isDiscounted) {
        this.isDiscounted = isDiscounted;
    }

    // ---SET A DISCOUNT ---
    public void applyDiscount(double percentage) {
        if (percentage > 0 && percentage <= 1) {
            BigDecimal discountFactor = new BigDecimal(String.valueOf(1.0 - percentage));
            this.unitPrice = this.unitPrice.multiply(discountFactor);
            this.isDiscounted = true;

            System.out.println("Current unit price: " + this.unitPrice);
            System.out.println("Is discounted: " + true);
        }
    }

    // --- CALCULATE PRICE WITH VAT (НДС) ---
    public void calculatePriceWithVAT(double vatRate) {
        BigDecimal vatFactor = new BigDecimal(String.valueOf(1.0 + vatRate));
        this.unitPrice = unitPrice.multiply(vatFactor);

        System.out.println("Current unit price: " + this.unitPrice);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId:" + productId +
                "Name:" + name +
                "Unit price:" + unitPrice +
                "Category:" + category +
                "Is discounted:" + isDiscounted +
                "}";
    }
}
