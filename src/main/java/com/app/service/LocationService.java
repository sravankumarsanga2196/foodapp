package com.app.service;

import java.util.List;

import com.app.dto.ApiResponse;
import com.app.dto.LocationRequest;
import com.app.dto.LocationResponse;

public interface LocationService {

    ApiResponse addLocation(LocationRequest request);

    List<LocationResponse> getNearby(Double lat, Double lng);

    List<LocationResponse> getWithinRadius(Double lat, Double lng, Double radius);

    List<LocationResponse> getByCity(String city);

    List<LocationResponse> getByArea(String area);

    List<LocationResponse> getDeliverable(Double lat, Double lng);

    LocationResponse getByRestaurant(String restaurantId);

    ApiResponse updateLocation(String id, LocationRequest request);

    ApiResponse deleteLocation(String id);
}