package com.inventory_forcasting.models;

import lombok.Builder;
import lombok.Data;
@Builder
@Data
public class ProductDTO {
    private Long id;
    private String name;
    private Categories category;
    private int currentStock;
    private int reorderThreshold;
    private int leadTimeDays;//ordered: DESC
    private double price;
    private ForecastDTO forecast;
}
