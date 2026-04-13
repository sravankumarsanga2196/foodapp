package com.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CartItemResponse {
    private String itemId;
    private String name;
    private Double price;
    private Integer quantity;
}