package com.inventory_forcasting.techs;

import com.inventory_forcasting.enties.Product;

public class LeadTimeValidation implements ProductValidationStrategy {
    @Override
    public boolean validate(Product product) {
        return product.getLeadTimeDays() >= 0;
    }

    @Override
    public String getMessage() {
        return "Lead time is not valid";
    }
}

