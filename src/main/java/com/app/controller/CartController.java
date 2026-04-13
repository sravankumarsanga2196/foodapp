package com.app.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.AddToCartRequest;
import com.app.dto.ApiResponse;
import com.app.dto.CartResponse;
import com.app.dto.RemoveCartRequest;
import com.app.dto.UpdateQuantityRequest;
import com.app.service.CartService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class CartController {

    private final CartService service;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> add(@RequestBody AddToCartRequest req) {
        return ResponseEntity.ok(service.addToCart(req));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartResponse> view(@PathVariable String userId) {
        return ResponseEntity.ok(service.viewCart(userId));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse> remove(@RequestBody RemoveCartRequest req) {
        return ResponseEntity.ok(service.removeItem(req));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse> update(@RequestBody UpdateQuantityRequest req) {
        return ResponseEntity.ok(service.updateQuantity(req));
    }

    @PutMapping("/increase")
    public ResponseEntity<ApiResponse> increase(@RequestBody UpdateQuantityRequest req) {
        return ResponseEntity.ok(service.increaseQuantity(req));
    }

    @PutMapping("/decrease")
    public ResponseEntity<ApiResponse> decrease(@RequestBody UpdateQuantityRequest req) {
        return ResponseEntity.ok(service.decreaseQuantity(req));
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<ApiResponse> clear(@PathVariable String userId) {
        return ResponseEntity.ok(service.clearCart(userId));
    }
}