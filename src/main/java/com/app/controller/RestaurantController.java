package com.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.ApiResponse;
import com.app.dto.RestaurantListResponse;
import com.app.dto.RestaurantRequest;
import com.app.dto.RestaurantResponse;
import com.app.service.RestaurantService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class RestaurantController {

    private final RestaurantService service;

    // ✅ ADD
    @PostMapping(value = "/addRestaurant", consumes = "multipart/form-data")
    public ApiResponse addRestaurant(@ModelAttribute RestaurantRequest request) {
        return service.addRestaurant(request);
    }

    // ✅ GET ALL
    @GetMapping
    public ResponseEntity<RestaurantListResponse> getAll() {
        return ResponseEntity.ok(service.getAllRestaurants());
    }

    // ✅ SEARCH
    @GetMapping("/search")
    public ResponseEntity<RestaurantListResponse> search(@RequestParam String name) {
        return ResponseEntity.ok(service.searchByName(name));
    }

    // ✅ FILTER BY RATING
    @GetMapping("/rating")
    public ResponseEntity<RestaurantListResponse> byRating(@RequestParam Double rating) {
        return ResponseEntity.ok(service.getByRating(rating));
    }

    // ✅ NEARBY
    @GetMapping("/nearby")
    public ResponseEntity<RestaurantListResponse> nearby(
            @RequestParam Double lat,
            @RequestParam Double lng) {
        return ResponseEntity.ok(service.getNearby(lat, lng));
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ✅ UPDATE
    @PutMapping(value = "/updateRestaurant/{id}", consumes = "multipart/form-data")
    public ApiResponse updateRestaurant(
            @PathVariable String id,
            @ModelAttribute RestaurantRequest request
    ) {
        return service.updateRestaurant(id, request);
    }

    // ✅ DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.deleteRestaurant(id));
    }

    // ✅ TOP RATED
    @GetMapping("/top-rated")
    public ResponseEntity<RestaurantListResponse> topRated() {
        return ResponseEntity.ok(service.getTopRated());
    }

    // ✅ TRENDING
    @GetMapping("/trending")
    public ResponseEntity<RestaurantListResponse> trending() {
        return ResponseEntity.ok(service.getTrending());
    }

    // ✅ LOCATION
    @GetMapping("/location")
    public ResponseEntity<RestaurantListResponse> byLocation(@RequestParam String location) {
        return ResponseEntity.ok(service.getByLocation(location));
    }

    // ✅ CITY (FIXED)
    @GetMapping("/city")
    public ResponseEntity<RestaurantListResponse> byCity(@RequestParam String city) {
        return ResponseEntity.ok(service.getByCity(city));
    }

    // ✅ STATE (FIXED)
    @GetMapping("/state")
    public ResponseEntity<RestaurantListResponse> byState(@RequestParam String state) {
        return ResponseEntity.ok(service.getByState(state));
    }

    // ✅ OPEN
    @GetMapping("/open")
    public ResponseEntity<RestaurantListResponse> openRestaurants() {
        return ResponseEntity.ok(service.getOpenRestaurants());
    }

    // ✅ CLOSED
    @GetMapping("/closed")
    public ResponseEntity<RestaurantListResponse> closedRestaurants() {
        return ResponseEntity.ok(service.getClosedRestaurants());
    }
}