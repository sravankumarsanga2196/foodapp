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
import com.app.dto.MenuListResponse;
import com.app.dto.MenuRequest;
import com.app.dto.MenuResponse;
import com.app.service.MenuService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/menu")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class MenuController {

    private final MenuService service;

    // ✅ ADD ITEM
    @PostMapping(value = "/add", consumes = "multipart/form-data")
    public ApiResponse addItem(@ModelAttribute MenuRequest request) {
        return service.addItem(request);
    }

    @GetMapping("/{restaurantId}")
    public ResponseEntity<MenuListResponse> getByRestaurant(@PathVariable String restaurantId) {
        return ResponseEntity.ok(service.getByRestaurant(restaurantId));
    }

    @GetMapping("/category")
    public ResponseEntity<MenuListResponse> byCategory(@RequestParam String category) {
        return ResponseEntity.ok(service.getByCategory(category));
    }

    @GetMapping("/top-rated")
    public ResponseEntity<MenuListResponse> topRated(@RequestParam Double rating) {
        return ResponseEntity.ok(service.getTopRated(rating));
    }

    @GetMapping("/trending")
    public ResponseEntity<MenuListResponse> trending() {
        return ResponseEntity.ok(service.getTrending());
    }

    @GetMapping("/search")
    public ResponseEntity<MenuListResponse> search(@RequestParam String name) {
        return ResponseEntity.ok(service.searchByName(name));
    }

    @GetMapping("/available")
    public ResponseEntity<MenuListResponse> available() {
        return ResponseEntity.ok(service.getAvailableItems());
    }

    @GetMapping("/price")
    public ResponseEntity<MenuListResponse> priceRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        return ResponseEntity.ok(service.getByPriceRange(min, max));
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<MenuResponse> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

 // ✅ UPDATE ITEM
    // ✅ UPDATE ITEM
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ApiResponse updateItem(
            @PathVariable String id,
            @ModelAttribute MenuRequest request
    ) {
        return service.updateItem(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable String id) {
        return ResponseEntity.ok(service.deleteItem(id));
    }
}