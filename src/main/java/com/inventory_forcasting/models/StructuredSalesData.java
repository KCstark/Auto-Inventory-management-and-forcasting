package com.inventory_forcasting.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StructuredSalesData {
    private LLModel model;
    private String productName;
    private String category;
    private Integer quantity;
    private LocalDate saleDate;
    private String location;
}
