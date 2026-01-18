package models.productModels;

import java.math.BigDecimal;
import java.math.RoundingMode;

import exceptions.InvalidSettersException;


abstract public class Product {
    protected int productId;
    protected String name;
    protected BigDecimal unitPrice;
    protected boolean isDiscounted;
    protected String category;

    // --- CONSTRUCTION ---
    public Product(int productId, String name, BigDecimal unitPrice, boolean isDiscounted, String category) {
        this.productId = productId;
        setName(name);
        setUnitPrice(unitPrice);
        setCategory(category);
        setIsDiscounted(isDiscounted);
    }

    // --- DEFAULT CONSTRUCTOR ---
    public Product(int i, String product1, BigDecimal bigDecimal) {
        this.isDiscounted = false;
    }


    // --- GETTERS  ---
    public int getId() { return productId; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public boolean isDiscounted() { return  isDiscounted; }

    // --- SETTERS ---
    public final void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidSettersException("Name can not be empty.");
        }
        this.name = name;
    }

    public final void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new InvalidSettersException("Category can not be empty.");
        }
        this.category = category;
    }

    public final void setUnitPrice(BigDecimal unitPrice) {
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidSettersException("Price can not be negative or empty.");
        }
        this.unitPrice = unitPrice.setScale(2, RoundingMode.HALF_UP);
    }

    public final void setIsDiscounted(boolean isDiscounted) {
        this.isDiscounted = isDiscounted;
    }

    /* --- ABSTRACT METHODS --- */
    public abstract String getSpecificDetails(); // FreshProduct -> weight, isBulk; FrozenProduct -> storageTemp, isDeepFreeze
    public abstract BigDecimal getTotalPrice(); // FreshProduct -> unitPrice * weight; FrozenProduct -> unitPrice
    public String getWeight() { return "-"; }
    public String getTemp() { return "-"; }

    // // ---SET A DISCOUNT ---
    // public void applyDiscount(double percentage) {
    //     if (percentage > 0 && percentage <= 1) {
    //         BigDecimal discountFactor = new BigDecimal(String.valueOf(1.0 - percentage));
    //         this.unitPrice = this.unitPrice.multiply(discountFactor).setScale(2, RoundingMode.HALF_UP);
    //         this.isDiscounted = true;

    //         System.out.println("Current unit price: " + this.unitPrice);
    //         System.out.println("Is discounted: " + true);
    //     }
    // }

    // // --- CALCULATE PRICE WITH VAT (НДС) ---
    // public void calculatePriceWithVAT(double vatRate) {
    //     BigDecimal vatFactor = new BigDecimal(String.valueOf(1.0 + vatRate));
    //     this.unitPrice = unitPrice.multiply(vatFactor).setScale(2, RoundingMode.HALF_UP);

    //     System.out.println("Current unit price: " + this.unitPrice);
    // }

    public String getDefrostAdvice() {
    return "No defrosting needed.";
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

