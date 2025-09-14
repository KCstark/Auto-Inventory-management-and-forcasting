package com.inventory_forcasting.models;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public enum Categories {
    ELECTRONICS,
    GROCERY,
    FURNITURE,
    CLOTHING,
    BOOKS,
    SOFTWARE;

    public static Set<String> getAll(){
        return Arrays.stream(values())
                .map(k->k.name())
                .collect(Collectors.toSet());
    }
}
