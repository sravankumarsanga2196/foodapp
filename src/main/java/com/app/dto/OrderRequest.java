package com.app.dto;

import lombok.Data;

@Data
public class OrderRequest {

    private String userId;
    private String address;
    private Double latitude;
    private Double longitude;
    private String paymentMethod;
}