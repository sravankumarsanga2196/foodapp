package com.app.dto;

import lombok.Data;

@Data
public class RemoveCartRequest {
    private String userId;
    private String itemId;
}