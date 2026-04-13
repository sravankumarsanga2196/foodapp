package com.app.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "menu_items")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String restaurantId;

    private String name;
    private String category;

    private Double price;

    // ✅ FIXED: store only URL
    private String foodPhotoUrl;

    private Double rating;

    private Boolean isAvailable;

    private Integer totalOrders;
}