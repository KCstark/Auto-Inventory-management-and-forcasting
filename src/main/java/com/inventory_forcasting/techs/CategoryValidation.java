package com.inventory_forcasting.techs;

import com.inventory_forcasting.enties.Product;
import com.inventory_forcasting.models.Categories;

import java.util.Set;

public class CategoryValidation implements ProductValidationStrategy {
    private static final Set<String> ALLOWED = Categories.getAll();
    public boolean validate(Product product) {
        return ALLOWED.contains(product.getCategory().name().toUpperCase());
    }

    @Override
    public String getMessage() {
        return "Category is not valid";
    }
}
