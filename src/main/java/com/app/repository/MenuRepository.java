package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.MenuItem;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuItem, String> {

    List<MenuItem> findByRestaurantId(String restaurantId);

    List<MenuItem> findByCategoryIgnoreCase(String category);

    List<MenuItem> findByRatingGreaterThanEqual(Double rating);

    List<MenuItem> findTop10ByOrderByTotalOrdersDesc();

    List<MenuItem> findByIsAvailableTrue();

    List<MenuItem> findByPriceBetween(Double min, Double max);

    List<MenuItem> findByNameContainingIgnoreCase(String name);
}