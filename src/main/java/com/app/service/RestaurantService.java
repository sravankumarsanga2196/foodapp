package com.app.service;

import com.app.dto.ApiResponse;
import com.app.dto.RestaurantListResponse;
import com.app.dto.RestaurantRequest;
import com.app.dto.RestaurantResponse;

public interface RestaurantService {

    ApiResponse addRestaurant(RestaurantRequest request);

    RestaurantListResponse getAllRestaurants();

    RestaurantListResponse searchByName(String name);

    RestaurantListResponse getByRating(Double rating);

    RestaurantListResponse getNearby(Double lat, Double lng);

    RestaurantResponse getById(String id);

    ApiResponse updateRestaurant(String id, RestaurantRequest request);

    ApiResponse deleteRestaurant(String id);

    RestaurantListResponse getTopRated();

    RestaurantListResponse getTrending();

    RestaurantListResponse getByLocation(String location);
    RestaurantListResponse getByCity(String city);
    RestaurantListResponse getByState(String state);

    RestaurantListResponse getOpenRestaurants();

    RestaurantListResponse getClosedRestaurants();
}