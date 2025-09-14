package com.inventory_forcasting.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SalesDTO {
    private Long id;
    private int quantity;
    private LocalDateTime saleDate;
    private Long productId;
    private Long userId;
}
