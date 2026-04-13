package com.app.controller;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.ApiResponse;
import com.app.dto.LocationRequest;
import com.app.dto.LocationResponse;
import com.app.service.LocationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class LocationController {

    private final LocationService service;

    @PostMapping
    public ResponseEntity<ApiResponse> add(@RequestBody LocationRequest req) {
        return ResponseEntity.ok(service.addLocation(req));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<LocationResponse>> nearby(
            @RequestParam Double lat,
            @RequestParam Double lng) {
        return ResponseEntity.ok(service.getNearby(lat, lng));
    }

    @GetMapping("/radius")
    public ResponseEntity<List<LocationResponse>> radius(
            @RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam Double radius) {
        return ResponseEntity.ok(service.getWithinRadius(lat, lng, radius));
    }

    @GetMapping("/city")
    public ResponseEntity<List<LocationResponse>> city(@RequestParam String city) {
        return ResponseEntity.ok(service.getByCity(city));
    }

    @GetMapping("/area")
    public ResponseEntity<List<LocationResponse>> area(@RequestParam String area) {
        return ResponseEntity.ok(service.getByArea(area));
    }

    @GetMapping("/deliverable")
    public ResponseEntity<List<LocationResponse>> deliverable(
            @RequestParam Double lat,
            @RequestParam Double lng) {
        return ResponseEntity.ok(service.getDeliverable(lat, lng));
    }

    @GetMapping("/restaurant/{id}")
    public ResponseEntity<LocationResponse> byRestaurant(@PathVariable String id) {
        return ResponseEntity.ok(service.getByRestaurant(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable String id,
                                              @RequestBody LocationRequest req) {
        return ResponseEntity.ok(service.updateLocation(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.deleteLocation(id));
    }
}