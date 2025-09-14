package com.inventory_forcasting.techs;

import com.inventory_forcasting.enties.Product;

public class NameValidation implements ProductValidationStrategy {
    @Override
    public boolean validate(Product product) {
        return product.getName() != null && !product.getName().trim().isEmpty();
    }

    @Override
    public String getMessage() {
        return "Product Name is not valid";
    }
}

