package com.inventory_forcasting.techs;

import com.inventory_forcasting.enties.Product;

import java.time.Duration;
import java.time.LocalDateTime;

public interface ForecastingStrategy {
    void calculateForecast(Product product, Duration forecastHorizon, LocalDateTime endDate);
}
