package factories;

import models.Customer;

public class CustomerFactory {

    public static Customer createCustomer(int id, String fullName, String phone, int loyaltyPoints, boolean isVip) {
        return new Customer(id, fullName, phone, loyaltyPoints, isVip);
    }
}
