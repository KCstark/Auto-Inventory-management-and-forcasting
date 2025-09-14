package com.inventory_forcasting.controllers;

import com.inventory_forcasting.enties.Forecast;
import com.inventory_forcasting.models.ForecastDTO;
import com.inventory_forcasting.service.ForecastService;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/forecasts")
//@RequiredArgsConstructor
public class ForecastsContrroller {

    @Autowired
    private ForecastService forecastService;

    //    /api/forecasts?start=date&end=date
    @GetMapping(params = {"start", "end"})
    public ResponseEntity<List<ForecastDTO>> getForecastsByDateRange(
            @RequestParam(value = "start", required = true)
            @Pattern(regexp="\\d{4}-\\d{2}-\\d{2}", message="Format must be yyyy-MM-dd") String start,
            @RequestParam(value = "end", required = true)
            @Pattern(regexp="\\d{4}-\\d{2}-\\d{2}", message="Format must be yyyy-MM-dd") String end) {
        return new ResponseEntity<>(forecastService.getForecastsByDateRange(start, end), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ForecastDTO>> getAllForecasts() {
        return new ResponseEntity<>(forecastService.getAllForecasts(), HttpStatus.OK);
    }
}
