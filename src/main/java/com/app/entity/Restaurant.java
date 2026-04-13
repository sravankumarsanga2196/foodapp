package com.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurants")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private String location;
    private String city;
    private String state;
    private Double latitude;
    private Double longitude;

    // ✅ STORE ONLY URL (NOT FILE)
    private String profilePhotoUrl;

    private Double rating;

    private Boolean isActive;

    private Boolean isOpen;
    

    private Integer totalOrders;
}