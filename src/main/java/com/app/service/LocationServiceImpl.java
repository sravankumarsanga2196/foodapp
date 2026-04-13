package com.app.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.dto.ApiResponse;
import com.app.dto.LocationRequest;
import com.app.dto.LocationResponse;
import com.app.entity.ErrorCode;
import com.app.entity.Location;
import com.app.exception.CustomException;
import com.app.repository.LocationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository repo;

    // ✅ ADD LOCATION
    @Override
    public ApiResponse addLocation(LocationRequest request) {

        validateCoordinates(request.getLatitude(), request.getLongitude());

        Location location = Location.builder()
                .restaurantId(request.getRestaurantId())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .deliveryRadius(request.getDeliveryRadius())
                .city(request.getCity())
                .area(request.getArea())
                .build();

        repo.save(location);

        return success("Location added successfully");
    }

    // ✅ NEARBY (5KM)
    @Override
    public List<LocationResponse> getNearby(Double lat, Double lng) {

        validateCoordinates(lat, lng);

        return repo.findAll().stream()
                .map(l -> map(l, lat, lng))
                .filter(r -> r.getDistance() <= 5)
                .toList();
    }

    // ✅ WITHIN RADIUS
    @Override
    public List<LocationResponse> getWithinRadius(Double lat, Double lng, Double radius) {

        validateCoordinates(lat, lng);

        return repo.findAll().stream()
                .map(l -> map(l, lat, lng))
                .filter(r -> r.getDistance() <= radius)
                .toList();
    }

    // ✅ BY CITY
    @Override
    public List<LocationResponse> getByCity(String city) {

        List<Location> list = repo.findByCityIgnoreCase(city);

        if (list.isEmpty()) {
            throw new CustomException(ErrorCode.LOCATION_NOT_FOUND, "No locations found for city");
        }

        return list.stream().map(l -> map(l, 0, 0)).toList();
    }

    // ✅ BY AREA
    @Override
    public List<LocationResponse> getByArea(String area) {

        List<Location> list = repo.findByAreaIgnoreCase(area);

        if (list.isEmpty()) {
            throw new CustomException(ErrorCode.LOCATION_NOT_FOUND, "No locations found for area");
        }

        return list.stream().map(l -> map(l, 0, 0)).toList();
    }

    // ✅ DELIVERABLE (FIXED BUG)
    @Override
    public List<LocationResponse> getDeliverable(Double lat, Double lng) {

        validateCoordinates(lat, lng);

        return repo.findAll().stream()
                .map(l -> {
                    double distance = calculateDistance(lat, lng, l.getLatitude(), l.getLongitude());

                    if (distance <= getRadius(l)) {
                        return LocationResponse.builder()
                                .restaurantId(l.getRestaurantId())
                                .latitude(l.getLatitude())
                                .longitude(l.getLongitude())
                                .distance(distance)
                                .build();
                    }
                    return null;
                })
                .filter(r -> r != null)
                .toList();
    }

    // ✅ GET BY RESTAURANT
    @Override
    public LocationResponse getByRestaurant(String restaurantId) {

        Location loc = repo.findAll().stream()
                .filter(l -> l.getRestaurantId().equals(restaurantId))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.LOCATION_NOT_FOUND, "Restaurant location not found"));

        return map(loc, 0, 0);
    }

    // ✅ UPDATE
    @Override
    public ApiResponse updateLocation(String id, LocationRequest request) {

        Location loc = repo.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.LOCATION_NOT_FOUND, "Location not found"));

        validateCoordinates(request.getLatitude(), request.getLongitude());

        loc.setLatitude(request.getLatitude());
        loc.setLongitude(request.getLongitude());
        loc.setCity(request.getCity());
        loc.setArea(request.getArea());
        loc.setDeliveryRadius(request.getDeliveryRadius());

        repo.save(loc);

        return success("Location updated successfully");
    }

    // ✅ DELETE (FIXED)
    @Override
    public ApiResponse deleteLocation(String id) {

        Location loc = repo.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.LOCATION_NOT_FOUND, "Location not found"));

        repo.delete(loc);

        return success("Location deleted successfully");
    }

    // 🔥 HELPER METHODS

    private LocationResponse map(Location l, double lat, double lng) {

        double distance = (lat == 0 && lng == 0)
                ? 0
                : calculateDistance(lat, lng, l.getLatitude(), l.getLongitude());

        return LocationResponse.builder()
                .restaurantId(l.getRestaurantId())
                .latitude(l.getLatitude())
                .longitude(l.getLongitude())
                .distance(distance)
                .build();
    }

    private double getRadius(Location l) {
        return l.getDeliveryRadius() != null ? l.getDeliveryRadius() : 5;
    }

    private void validateCoordinates(Double lat, Double lng) {

        if (lat == null || lng == null ||
            lat < -90 || lat > 90 ||
            lng < -180 || lng > 180) {

            throw new CustomException(ErrorCode.INVALID_COORDINATES, "Invalid latitude/longitude");
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        final int R = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        return 2 * R * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }

    private ApiResponse success(String msg) {
        return ApiResponse.builder()
                .status("SUCCESS")
                .message(msg)
                .build();
    }
}