package com.app.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartResponse {
    private String status;
    private List<CartItemResponse> cartItems;
    private Double totalAmount;
}