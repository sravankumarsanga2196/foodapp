package com.app.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class MenuRequest {

    private String restaurantId;
    private String name;
    private String category;
    private Double price;

    // only request layer
    private MultipartFile foodPhoto;

    private Double rating;  
}