package com.inventory_forcasting.controllers;

import com.inventory_forcasting.models.SalesDTO;
import com.inventory_forcasting.service.SalesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/sales")
public class SalesController {

//    POST /api/sales (validates product exists, triggers forecast)

    private final SalesService salesService;

    public SalesController(SalesService salesService) {
        this.salesService = salesService;
    }

    @PostMapping
    public ResponseEntity<Object> addSalesData(@RequestBody SalesDTO sales) {
        return ResponseEntity.status(HttpStatus.CREATED).body(salesService.addSalesData(sales));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Object> getAllSalesData() {
        return ResponseEntity.ok(salesService.getAllSalesData());
    }
}
