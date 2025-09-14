package com.inventory_forcasting.service;

import com.inventory_forcasting.enties.Forecast;
import com.inventory_forcasting.enties.Sales;
import com.inventory_forcasting.models.Algorithm;
import com.inventory_forcasting.models.EventType;
import com.inventory_forcasting.models.ForecastDTO;
import com.inventory_forcasting.repo.ForecastRepository;
import com.inventory_forcasting.techs.ForcastDtoAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class ForecastService {
    private List<Algorithm> algorithms;
    private final ForcastEngine forcastEngine;
    private final ForecastRepository forecastRepository;
    private final ForcastDtoAdapter forcastDtoAdapter;
    private final AuditService auditService;

    public ForecastService(ForcastEngine forcastEngine, ForecastRepository forecastRepository,
                           ForcastDtoAdapter forcastDtoAdapter,AuditService auditService) {
        this.forcastEngine = forcastEngine;
        this.forecastRepository = forecastRepository;
        this.forcastDtoAdapter = forcastDtoAdapter;
        this.auditService = auditService;
        algorithms=List.of(Algorithm.values());
    }

    public void notifyForecastEngine(Sales sales) {
        Thread thread = new Thread(() -> call(sales,Duration.ofDays(7), LocalDateTime.now()));
        thread.start();
        auditService.logEvent(EventType.FORECAST_GENERATING, "Auto-generating forecast", "Forecast generation in progress");
        log.info("Forecasting engine notified");
    }

    private void call(Sales sales,Duration duration,LocalDateTime endDate) {
        log.info("calling forecast engine");
        for(Algorithm algo : algorithms) {
                Thread t = new Thread(()->forcastEngine.work(sales,algo,duration,endDate));
                t.start();
        }
    }

    public List<ForecastDTO> getAllForecasts() {
        log.info("Fetching all forecasts");
        List<Forecast> forecasts = forecastRepository.findAll();
        List<ForecastDTO> forecastDTOs = forecasts.stream().map(forcastDtoAdapter::convertToDto).toList();
        auditService.logEvent(EventType.FORECAST_GET_ALL, "GET /forecasts", "Forecasts retrieved successfully");
        return forecastDTOs;
    }

    public List<ForecastDTO> getForecastsByDateRange(String start, String end) {
        //start and end are in yyyy-MM-dd format only
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        start=start+" 00:00:00";
        end=end+" 23:59:59";
        LocalDateTime startDate = LocalDateTime.parse(start, formatter);
        LocalDateTime endDate = LocalDateTime.parse(end, formatter);

        auditService.logEvent(EventType.FORECAST_GET_BY_DATE_RANGE, "GET /forecasts?start="+start+"&end="+end, "Forecasts retrieved successfully");

        return forecastRepository.findByDateRange(startDate, endDate).stream()
                .map(forcastDtoAdapter::convertToDto).toList();
//        return null;
    }
}
