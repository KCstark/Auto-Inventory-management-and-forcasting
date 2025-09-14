package com.inventory_forcasting.techs;

import com.inventory_forcasting.enties.Product;

public class ThresholdValidation implements ProductValidationStrategy {
    public boolean validate(Product product) {
        return product.getReorderThreshold() < product.getCurrentStock();
    }
    @Override
    public String getMessage() {
        return "Reorder Threshold is not valid";
    }
}
