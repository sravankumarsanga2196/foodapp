package com.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantResponse {

    private String restaurantId;
    private String name;
    private String location;
    private Double rating;
    private String profilePhotoUrl;
}