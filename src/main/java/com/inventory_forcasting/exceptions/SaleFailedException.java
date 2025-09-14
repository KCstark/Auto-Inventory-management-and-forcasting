package com.inventory_forcasting.exceptions;

public class SaleFailedException extends RuntimeException {
    public SaleFailedException(String message) {
        super(message);
    }
}
