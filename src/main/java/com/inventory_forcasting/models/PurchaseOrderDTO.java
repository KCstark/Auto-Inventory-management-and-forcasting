package com.inventory_forcasting.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PurchaseOrderDTO {
    private Long id;
    private Long productId;
    private LocalDateTime orderDate;//ordered index : asc
    private Double quantityOrdered;
    private LocalDateTime expectedArrivalDate;//order index : asc
    private Status status;
}
