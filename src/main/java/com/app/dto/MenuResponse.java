package com.app.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuResponse {

    private String itemId;
    private String name;
    private String category;
    private Double price;
    private Double rating;
    private String foodPhotoUrl;
}