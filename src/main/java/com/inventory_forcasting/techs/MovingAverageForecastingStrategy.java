package com.inventory_forcasting.techs;

import com.inventory_forcasting.enties.Forecast;
import com.inventory_forcasting.enties.Product;
import com.inventory_forcasting.enties.Sales;
import com.inventory_forcasting.models.Algorithm;
import com.inventory_forcasting.repo.ForecastRepository;
import com.inventory_forcasting.repo.ProductRepository;
import com.inventory_forcasting.repo.SalesRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Component
//@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
public class MovingAverageForecastingStrategy implements ForecastingStrategy{

    private final SalesRepository salesRepository;
    private final ForecastRepository forecastRepository;
    private final ProductRepository productRepository;

    // filter last forecastHorizon days from endDate, compute average quantity, save forecast, from sales repo
    @Override
    public void calculateForecast(Product product, Duration forecastHorizon, LocalDateTime endDate) {
        log.info("Forecasting for Moving Average");
        List<Sales> sales = salesRepository.findByProductIdAndSaleDateBetween(product.getId(), endDate.minus(forecastHorizon), endDate);
        double averageQuantity = sales.stream().mapToDouble(Sales::getQuantity).average().orElse(0);
        Forecast forecast = Forecast.builder()
                .product(product)
                .forecastDate(LocalDateTime.now())
                .algorithmUsed(Algorithm.MOVING_AVERAGE)
                .predictedDemand(Math.floor(averageQuantity))
                .build();

        log.info("Forcast done for product: /n {}",forecastRepository.save(forecast));
    }
}
