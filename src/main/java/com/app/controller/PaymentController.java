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
import com.app.dto.PaymentCallbackRequest;
import com.app.dto.PaymentListResponse;
import com.app.dto.PaymentRequest;
import com.app.dto.PaymentResponse;
import com.app.service.PaymentService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class PaymentController {

    private final PaymentService service;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiate(@RequestBody PaymentRequest req) {
        return ResponseEntity.ok(service.initiatePayment(req));
    }

    @PostMapping("/callback")
    public ResponseEntity<ApiResponse> callback(@RequestBody PaymentCallbackRequest req) {
        return ResponseEntity.ok(service.handleCallback(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> get(@PathVariable String id) {
        return ResponseEntity.ok(service.getPayment(id));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentListResponse> byOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(service.getByOrder(orderId));
    }

    @GetMapping("/status")
    public ResponseEntity<PaymentListResponse> byStatus(@RequestParam String status) {
        return ResponseEntity.ok(service.getByStatus(status));
    }

    @PutMapping("/retry/{id}")
    public ResponseEntity<ApiResponse> retry(@PathVariable String id) {
        return ResponseEntity.ok(service.retryPayment(id));
    }

    @PutMapping("/cancel/{id}")
    public ResponseEntity<ApiResponse> cancel(@PathVariable String id) {
        return ResponseEntity.ok(service.cancelPayment(id));
    }
}