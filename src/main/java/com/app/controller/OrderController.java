package com.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.ApiResponse;
import com.app.dto.OrderListResponse;
import com.app.dto.OrderRequest;
import com.app.dto.OrderResponse;
import com.app.service.OrderService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<OrderResponse> place(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(service.placeOrder(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<OrderListResponse> getOrders(@PathVariable String userId) {
        return ResponseEntity.ok(service.getOrders(userId));
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getOrderById(id));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<ApiResponse> cancel(@PathVariable String id) {
        return ResponseEntity.ok(service.cancelOrder(id));
    }

    @PutMapping("/status")
    public ResponseEntity<ApiResponse> updateStatus(@RequestParam String id,
                                                    @RequestParam String status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }

    @GetMapping("/status")
    public ResponseEntity<OrderListResponse> byStatus(@RequestParam String status) {
        return ResponseEntity.ok(service.getByStatus(status));
    }

    @GetMapping("/track/{id}")
    public ResponseEntity<OrderResponse> track(@PathVariable String id) {
        return ResponseEntity.ok(service.trackOrder(id));
    }
}