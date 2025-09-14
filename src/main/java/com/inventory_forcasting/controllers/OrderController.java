package com.inventory_forcasting.controllers;

import com.inventory_forcasting.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/auto")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> autoGenerateOrders() {
        return ResponseEntity.ok(orderService.autoGenerateOrders());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PutMapping("/{id}/receive")
    public ResponseEntity<Object> receiveOrder(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.receiveOrder(id));
    }

}
