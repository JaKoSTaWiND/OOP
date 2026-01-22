package factories;

import models.customerModels.BaseCustomer;
import models.customerModels.Customer;

public class CustomerFactory {

    public static Customer createCustomer(
            int customerId,
            String fullName,
            String phone,
            int loyaltyPoints,
            boolean isVip
        ) {
        return new BaseCustomer.Builder()
                .customerId(customerId)
                .fullName(fullName)
                .phone(phone)
                .loyaltyPoints(loyaltyPoints)
                .isVip(isVip)
                .build();
    }
}
