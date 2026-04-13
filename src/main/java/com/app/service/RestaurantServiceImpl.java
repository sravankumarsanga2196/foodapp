package com.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.dto.*;
import com.app.entity.ErrorCode;
import com.app.entity.Restaurant;
import com.app.exception.CustomException;
import com.app.repository.RestaurantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository repository;
    private final FileUploadService fileUploadService;

    // ✅ ADD RESTAURANT
    @Override
    public ApiResponse addRestaurant(RestaurantRequest request) {

        String imageUrl = null;

        if (request.getProfilePhoto() != null && !request.getProfilePhoto().isEmpty()) {
            imageUrl = fileUploadService.uploadFile(request.getProfilePhoto(), "restaurants");
        }

        Restaurant restaurant = Restaurant.builder()
                .name(request.getName())
                .location(request.getLocation())
                .city(request.getCity())
                .state(request.getState())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .profilePhotoUrl(imageUrl)
                .rating(request.getRating() != null ? request.getRating() : 0.0)
                .isActive(true)
                .isOpen(true)
                .totalOrders(0)
                .build();

        repository.save(restaurant);

        return ApiResponse.builder()
                .status("SUCCESS")
                .message("Restaurant added successfully")
                .build();
    }

    // ✅ GET ALL
    @Override
    public RestaurantListResponse getAllRestaurants() {

        List<Restaurant> list = repository.findByIsActiveTrue();

        if (list.isEmpty()) {
            throw new CustomException(ErrorCode.RESTAURANT_NOT_FOUND, "No restaurants found");
        }

        return mapToListResponse(list);
    }

    // ✅ SEARCH BY NAME
    @Override
    public RestaurantListResponse searchByName(String name) {

        List<Restaurant> list = repository.findByNameContainingIgnoreCaseAndIsActiveTrue(name);

        if (list.isEmpty()) {
            throw new CustomException(ErrorCode.RESTAURANT_NOT_FOUND, "No restaurant found");
        }

        return mapToListResponse(list);
    }

    // ✅ FILTER BY RATING
    @Override
    public RestaurantListResponse getByRating(Double rating) {

        List<Restaurant> list = repository.findByRatingGreaterThanEqualAndIsActiveTrue(rating);

        if (list.isEmpty()) {
            throw new CustomException(ErrorCode.RESTAURANT_NOT_FOUND, "No restaurants found");
        }

        return mapToListResponse(list);
    }

    // ✅ GET BY ID
    @Override
    public RestaurantResponse getById(String id) {

        Restaurant r = repository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESTAURANT_NOT_FOUND, "Restaurant not found"));

        return mapToResponse(r);
    }

    // ✅ UPDATE
    @Override
    public ApiResponse updateRestaurant(String id, RestaurantRequest request) {

        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESTAURANT_NOT_FOUND, "Restaurant not found"));

        restaurant.setName(request.getName());
        restaurant.setLocation(request.getLocation());
        restaurant.setCity(request.getCity());
        restaurant.setState(request.getState());
        restaurant.setLatitude(request.getLatitude());
        restaurant.setLongitude(request.getLongitude());

        if (request.getRating() != null) {
            restaurant.setRating(request.getRating());
        }

        // image update
        if (request.getProfilePhoto() != null && !request.getProfilePhoto().isEmpty()) {
            String imageUrl = fileUploadService.uploadFile(request.getProfilePhoto(), "restaurants");
            restaurant.setProfilePhotoUrl(imageUrl);
        }

        repository.save(restaurant);

        return ApiResponse.builder()
                .status("SUCCESS")
                .message("Restaurant updated successfully")
                .build();
    }

    // ✅ DELETE (SOFT DELETE)
    @Override
    public ApiResponse deleteRestaurant(String id) {

        Restaurant r = repository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESTAURANT_NOT_FOUND, "Restaurant not found"));

        r.setIsActive(false);
        repository.save(r);

        return ApiResponse.builder()
                .status("SUCCESS")
                .message("Restaurant deleted")
                .build();
    }

    // ✅ FILTER BY LOCATION
    @Override
    public RestaurantListResponse getByLocation(String location) {

        List<Restaurant> list = repository
                .findByLocationContainingIgnoreCaseAndIsActiveTrue(location);

        if (list.isEmpty()) {
            throw new CustomException(ErrorCode.RESTAURANT_NOT_FOUND, "No restaurants found in this location");
        }

        return mapToListResponse(list);
    }

    // ✅ FILTER BY CITY
    @Override
    public RestaurantListResponse getByCity(String city) {

        List<Restaurant> list = repository
                .findByCityContainingIgnoreCaseAndIsActiveTrue(city);

        if (list.isEmpty()) {
            throw new CustomException(ErrorCode.RESTAURANT_NOT_FOUND, "No restaurants found in this city");
        }

        return mapToListResponse(list);
    }

    // ✅ FILTER BY STATE
    @Override
    public RestaurantListResponse getByState(String state) {

        List<Restaurant> list = repository
                .findByStateContainingIgnoreCaseAndIsActiveTrue(state);

        if (list.isEmpty()) {
            throw new CustomException(ErrorCode.RESTAURANT_NOT_FOUND, "No restaurants found in this state");
        }

        return mapToListResponse(list);
    }

    // ✅ TOP RATED
    @Override
    public RestaurantListResponse getTopRated() {

        List<Restaurant> list = repository
                .findTop10ByIsActiveTrueOrderByRatingDesc();

        if (list.isEmpty()) {
            throw new CustomException(ErrorCode.RESTAURANT_NOT_FOUND, "No top rated restaurants found");
        }

        return mapToListResponse(list);
    }

    // ✅ TRENDING
    @Override
    public RestaurantListResponse getTrending() {

        List<Restaurant> list = repository
                .findTop10ByIsActiveTrueOrderByTotalOrdersDesc();

        if (list.isEmpty()) {
            throw new CustomException(ErrorCode.RESTAURANT_NOT_FOUND, "No trending restaurants found");
        }

        return mapToListResponse(list);
    }

    // ✅ OPEN
    @Override
    public RestaurantListResponse getOpenRestaurants() {

        List<Restaurant> list = repository.findByIsOpenTrueAndIsActiveTrue();

        return mapToListResponse(list);
    }

    // ✅ CLOSED
    @Override
    public RestaurantListResponse getClosedRestaurants() {

        List<Restaurant> list = repository.findByIsOpenFalseAndIsActiveTrue();

        return mapToListResponse(list);
    }

    // 🔥 COMMON MAPPER
    private RestaurantListResponse mapToListResponse(List<Restaurant> list) {

        List<RestaurantResponse> responses = list.stream()
                .map(this::mapToResponse)
                .toList();

        return RestaurantListResponse.builder()
                .status("SUCCESS")
                .restaurants(responses)
                .build();
    }

    private RestaurantResponse mapToResponse(Restaurant r) {

        return RestaurantResponse.builder()
                .restaurantId(r.getId())
                .name(r.getName())
                .location(r.getLocation())
                .rating(r.getRating())
                .profilePhotoUrl(r.getProfilePhotoUrl())
                .build();
    }

    @Override
    public RestaurantListResponse getNearby(Double lat, Double lng) {

        List<Restaurant> all = repository.findByIsActiveTrue();

        List<Restaurant> nearby = all.stream()
                .filter(r -> r.getLatitude() != null && r.getLongitude() != null)
                .filter(r -> distance(lat, lng, r.getLatitude(), r.getLongitude()) <= 5) // 5 KM radius
                .toList();

        if (nearby.isEmpty()) {
            throw new CustomException(ErrorCode.RESTAURANT_NOT_FOUND, "No nearby restaurants found");
        }

        return mapToListResponse(nearby);
    }


    // 🔥 Haversine Formula
    private double distance(double lat1, double lon1, double lat2, double lon2) {

        double R = 6371; // Earth radius in KM

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) *
                Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2);

        return 2 * R * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }


}