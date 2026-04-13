package com.app.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class RestaurantRequest {

    private String name;
    private String location;
    private Double latitude;
    private Double longitude;
    private String city;
    private String state;
    private MultipartFile profilePhoto; // ✅ correct for multipart

    private Double rating;
}