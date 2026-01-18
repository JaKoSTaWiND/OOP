package models;

import exceptions.InvalidSettersException;

public class Customer {
    private final int customerId;
    private String fullName;
    private String phone;
    private int loyaltyPoints;
    private boolean isVip;

    // --- CONSTRUCTION ---
    public Customer(int customerId, String fullName, String phone, int loyaltyPoints, boolean isVip) {
        this.customerId = customerId;
        setFullName(fullName);
        setPhone(phone);
        setLoyaltyPoints(loyaltyPoints);
        setIsVip(isVip);
    }

    // --- DEFAULT CONSTRUCTION ---
    public Customer(int customerId) {
        this.customerId = customerId;
        this.loyaltyPoints = 0;
        this.isVip = false;
    }

    // --- GETTERS  ---
    public int getId() { return customerId; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public int getLoyaltyPoints() { return loyaltyPoints; }
    public boolean isVip() { return isVip; }

    // --- SETTERS ---
    public final void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new InvalidSettersException("Full name can not be empty.");
        } else {
            this.fullName = fullName;
        }
    }

    public final void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new InvalidSettersException("Phone can not be empty.");
        }
        
        int phoneLength = phone.trim().length();
        if (phoneLength != 11 && phoneLength != 12) {
            throw new InvalidSettersException("Phone must include 11 digits (KZ (8...)) or 12 digits (INTER (+7...)).");
        }
        
        this.phone = phone.trim();
    }

    public final void setLoyaltyPoints(int loyaltyPoints) {
        if (loyaltyPoints < 0) {
            throw new InvalidSettersException("Loyalty points can not be negative.");
        } else {
            this.loyaltyPoints = loyaltyPoints;
        }
    }

    public final void setIsVip(boolean isVip) {
        this.isVip = isVip;
    }

    // --- TO STRING ---
    @Override
    public String toString() {
        return "Customer{" +
                "customerId:" + customerId +
                "Full name:" + fullName +
                "Phone:" + phone +
                "Loyalty points:" + loyaltyPoints +
                "Is VIP:" + isVip +
                "}";
    }
}
