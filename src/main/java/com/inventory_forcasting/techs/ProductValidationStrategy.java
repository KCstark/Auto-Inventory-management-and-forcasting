package com.inventory_forcasting.techs;

import com.inventory_forcasting.enties.Product;

public interface ProductValidationStrategy {
    boolean validate(Product product);

    String getMessage();
}