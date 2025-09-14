package com.inventory_forcasting.techs;

import com.inventory_forcasting.enties.Product;

public class PriceValidation implements ProductValidationStrategy {
    @Override
    public boolean validate(Product product) {
        return product.getPrice() > 0;
    }
    @Override
    public String getMessage() {
        return "Product Price is not valid";
    }
}

