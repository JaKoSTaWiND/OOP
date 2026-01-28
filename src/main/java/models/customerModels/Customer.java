package models.customerModels;

import org.immutables.value.Value;

import exceptions.InvalidSettersException;
import models.ImmutableStyle;

@ImmutableStyle
@Value.Immutable
public abstract class Customer {

    public abstract int customerId();
    public abstract String fullName();
    public abstract String phone();

    // --- DEFAULTS ---
    @Value.Default
    public int loyaltyPoints() {
        return 0;
    }
    @Value.Default
    public boolean isVip() {
        return false;
    }

    // --- VALIDATION ---
    @Value.Check
    protected void validateFullName() { // validate fullName
        if (fullName().trim().isEmpty()) {
            throw new InvalidSettersException("Full name can not be empty.");
        }
    }
    @Value.Check
    protected void validatePhone() { // validate phone
        if (phone().trim().isEmpty()) {
            throw new InvalidSettersException("Phone can not be empty.");
        }
        int phoneLength = phone().trim().length();
        if (phoneLength != 11 && phoneLength != 12) {
            throw new InvalidSettersException("Phone must include 11 digits (KZ (8...)) or 12 digits (INTER (+7...)).");
        }
    }
    @Value.Check
    protected void validateLoyaltyPoints() { // validate loyaltyPoints
        if (loyaltyPoints() < 0) {
            throw new InvalidSettersException("Loyalty points can not be negative.");
        }
    }
}
