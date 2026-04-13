package com.app.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.app.dto.ApiResponse;
import com.app.dto.MenuListResponse;
import com.app.dto.MenuRequest;
import com.app.dto.MenuResponse;
import com.app.entity.ErrorCode;
import com.app.entity.MenuItem;
import com.app.exception.CustomException;
import com.app.repository.MenuRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository repository;
    private final FileUploadService  fileUploadService;
 // ================= ADD ITEM =================
    public ApiResponse addItem(MenuRequest request) {

        String imageUrl = uploadImage(request);

        MenuItem item = MenuItem.builder()
                .restaurantId(request.getRestaurantId())
                .name(request.getName())
                .category(request.getCategory())
                .price(request.getPrice())
                .foodPhotoUrl(imageUrl)
                .rating(request.getRating() != null ? request.getRating() : 0.0)
                .isAvailable(true)
                .totalOrders(0)
                .build();

        repository.save(item);

        return ApiResponse.builder()
                .status("SUCCESS")
                .message("Food item added successfully")
                .build();
    }

    @Override
    public MenuListResponse getByRestaurant(String restaurantId) {

        List<MenuItem> list = repository.findByRestaurantId(restaurantId);

        if (list.isEmpty()) {
            throw new CustomException(ErrorCode.FOOD_ITEM_NOT_FOUND, "No items found for this restaurant");
        }

        return mapToList(list);
    }

    @Override
    public MenuListResponse getByCategory(String category) {

        List<MenuItem> list = repository.findByCategoryIgnoreCase(category);

        if (list.isEmpty()) {
            throw new CustomException(ErrorCode.FOOD_ITEM_NOT_FOUND, "No items found in this category");
        }

        return mapToList(list);
    }

    @Override
    public MenuListResponse getTopRated(Double rating) {

        List<MenuItem> list = repository.findByRatingGreaterThanEqual(rating);

        return mapToList(list);
    }

    @Override
    public MenuListResponse getTrending() {

        List<MenuItem> list = repository.findTop10ByOrderByTotalOrdersDesc();

        return mapToList(list);
    }

    @Override
    public MenuListResponse searchByName(String name) {

        List<MenuItem> list = repository.findByNameContainingIgnoreCase(name);

        return mapToList(list);
    }

    @Override
    public MenuListResponse getAvailableItems() {

        List<MenuItem> list = repository.findByIsAvailableTrue();

        return mapToList(list);
    }

    @Override
    public MenuListResponse getByPriceRange(Double min, Double max) {

        List<MenuItem> list = repository.findByPriceBetween(min, max);

        return mapToList(list);
    }

    @Override
    public MenuResponse getById(String id) {

        MenuItem item = repository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_ITEM_NOT_FOUND, "Item not found"));

        return mapToResponse(item);
    }

    // ================= UPDATE ITEM =================
    public ApiResponse updateItem(String id, MenuRequest request) {

        MenuItem item = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        item.setName(request.getName());
        item.setCategory(request.getCategory());
        item.setPrice(request.getPrice());
        item.setRating(request.getRating());

        String imageUrl = uploadImage(request);
        if (imageUrl != null) {
            item.setFoodPhotoUrl(imageUrl);
        }

        repository.save(item);

        return ApiResponse.builder()
                .status("SUCCESS")
                .message("Item updated successfully")
                .build();
    }

    // ================= COMMON IMAGE METHOD =================
    private String uploadImage(MenuRequest request) {
        if (request.getFoodPhoto() != null && !request.getFoodPhoto().isEmpty()) {
            return fileUploadService.uploadFile(request.getFoodPhoto(), "menu");
        }
        return null;
    }
    @Override
    public ApiResponse deleteItem(String id) {

        MenuItem item = repository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_ITEM_NOT_FOUND, "Item not found"));

        item.setIsAvailable(false);

        repository.save(item);

        return ApiResponse.builder()
                .status("SUCCESS")
                .message("Item removed")
                .build();
    }

    // 🔥 MAPPERS

    private MenuListResponse mapToList(List<MenuItem> list) {

        List<MenuResponse> items = list.stream()
                .map(this::mapToResponse)
                .toList();

        return MenuListResponse.builder()
                .status("SUCCESS")
                .items(items)
                .build();
    }

    private MenuResponse mapToResponse(MenuItem item) {

        return MenuResponse.builder()
                .itemId(item.getId())
                .name(item.getName())
                .category(item.getCategory())
                .price(item.getPrice())
                .rating(item.getRating())
                .foodPhotoUrl(item.getFoodPhotoUrl())
                .build();
    }
}