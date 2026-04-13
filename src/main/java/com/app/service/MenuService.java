package com.app.service;

import com.app.dto.ApiResponse;
import com.app.dto.MenuListResponse;
import com.app.dto.MenuRequest;
import com.app.dto.MenuResponse;

public interface MenuService {

    ApiResponse addItem(MenuRequest request);

    MenuListResponse getByRestaurant(String restaurantId);

    MenuListResponse getByCategory(String category);

    MenuListResponse getTopRated(Double rating);

    MenuListResponse getTrending();

    MenuListResponse searchByName(String name);

    MenuListResponse getAvailableItems();

    MenuListResponse getByPriceRange(Double min, Double max);

    MenuResponse getById(String id);

    ApiResponse updateItem(String id, MenuRequest request);

    ApiResponse deleteItem(String id);
}