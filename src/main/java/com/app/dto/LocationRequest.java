package com.app.dto;

import lombok.Data;

@Data
public class LocationRequest {

    private String restaurantId;
    private Double latitude;
    private Double longitude;
    private Double deliveryRadius;
    private String city;
    private String area;
}