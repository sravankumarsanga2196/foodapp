package com.app.dto;

import lombok.Data;

@Data
public class UpdateQuantityRequest {
    private String userId;
    private String itemId;
    private Integer quantity;
}