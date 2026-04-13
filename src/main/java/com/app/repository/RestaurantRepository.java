package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Restaurant;

import java.util.List;
public interface RestaurantRepository extends JpaRepository<Restaurant, String> {

    List<Restaurant> findByIsActiveTrue();

    List<Restaurant> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);

    List<Restaurant> findByRatingGreaterThanEqualAndIsActiveTrue(Double rating);

    List<Restaurant> findByLocationContainingIgnoreCaseAndIsActiveTrue(String location);

    List<Restaurant> findByCityContainingIgnoreCaseAndIsActiveTrue(String city);

    List<Restaurant> findByStateContainingIgnoreCaseAndIsActiveTrue(String state);

    List<Restaurant> findTop10ByIsActiveTrueOrderByRatingDesc();

    List<Restaurant> findTop10ByIsActiveTrueOrderByTotalOrdersDesc();

    List<Restaurant> findByIsOpenTrueAndIsActiveTrue();

    List<Restaurant> findByIsOpenFalseAndIsActiveTrue();
}