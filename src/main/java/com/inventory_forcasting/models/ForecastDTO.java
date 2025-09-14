package com.inventory_forcasting.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data@Builder
public class ForecastDTO {
    private Long id;
    private Long productId;
    private LocalDateTime forecastDate;
    private Double predictedDemand;
    private Algorithm algorithmUsed;
}
