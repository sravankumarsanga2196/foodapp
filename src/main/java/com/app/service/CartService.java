package com.app.service;

import com.app.dto.AddToCartRequest;
import com.app.dto.ApiResponse;
import com.app.dto.CartResponse;
import com.app.dto.RemoveCartRequest;
import com.app.dto.UpdateQuantityRequest;

public interface CartService {

    ApiResponse addToCart(AddToCartRequest request);

    CartResponse viewCart(String userId);

    ApiResponse removeItem(RemoveCartRequest request);

    ApiResponse updateQuantity(UpdateQuantityRequest request);

    ApiResponse clearCart(String userId);

    ApiResponse decreaseQuantity(UpdateQuantityRequest request);

    ApiResponse increaseQuantity(UpdateQuantityRequest request);
}