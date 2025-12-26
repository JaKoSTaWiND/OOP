package models;

public class Customer {
    private final int customerId;
    private String fullName;
    private String phone;
    private int loyaltyPoints;
    private boolean isVip;

    // --- CONSTRUCTION ---
    public Customer(int customerId, String fullName, String phone, int loyaltyPoints, boolean isVip) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.phone = phone;
        this.loyaltyPoints = loyaltyPoints;
        this.isVip = isVip;
    }

    // --- DEFAULT CONSTRUCTION ---
    public Customer(int customerId) {
        this.customerId = customerId;
        this.loyaltyPoints = 0;
        this.isVip = false;
    }

    // --- GETTERS  ---
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public int getLoyaltyPoints() { return loyaltyPoints; }
    public boolean isVip() { return isVip; }

    // --- SETTERS ---
    public void setFullName(String fullName) {
        if (fullName != null) {
            this.fullName = fullName;
        } else {
            System.out.println("Invalid full name");
        }
    }

    public void setPhone(String phone) {
        if (phone != null && phone.length() == 12) {
            this.phone = phone;
        } else {
            System.out.println("Invalid phone");
        }
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        if (loyaltyPoints >= 0) {
            this.loyaltyPoints = loyaltyPoints;
        } else {
            System.out.println("Invalid loyalty points");
        }
    }

    public void setIsVip(boolean isVip) {
        this.isVip = isVip;
    }

    // --- ADD LOYALTY POINTS ---
    public void addLoyaltyPoints(double amount) {
        int pointsToAdd = (int) (amount / 100.0);
        this.loyaltyPoints = this.loyaltyPoints + pointsToAdd;
        System.out.println("Current loyalty points: " + this.loyaltyPoints);
    }

    // --- USE POINTS TO DISCOUNT ---
    public boolean pointsToDiscount(int pointsToUse, double price) {
        double currentPrice = (price);
        double resultPrice;

        if (pointsToUse > 0 && this.loyaltyPoints >= pointsToUse) {
            resultPrice = currentPrice - pointsToUse;
            this.loyaltyPoints = this.loyaltyPoints - pointsToUse;
            System.out.println("Price with discount: " + resultPrice);
            return true;
        }
        System.out.println("Not enough loyalty points. Available: " + this.loyaltyPoints);
        return false;
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
