package com.inventory_forcasting.service;

import com.inventory_forcasting.enties.Sales;
import com.inventory_forcasting.models.Algorithm;
import com.inventory_forcasting.techs.MovingAverageForecastingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


import java.time.Duration;
import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class ForcastEngine {
    private final MovingAverageForecastingStrategy movingAverageForecastingStrategy;

    public void work(Sales sales, Algorithm algorithm, Duration forecastHorizon, LocalDateTime endDate) {
        log.info("Forecasting engine running");
        switch (algorithm) {
            case MOVING_AVERAGE:
                movingAverageForecastingStrategy.calculateForecast(sales.getProduct(), forecastHorizon, endDate);
                break;
            case EXPONENTIAL_SMOOTHING:
                System.out.println("EXPONENTIAL_SMOOTHING Not implemented yet");
                break;
            case CUSTOM:
                System.out.println("CUSTOM algorithm not implemented yet");
                break;
        }
    }
}
