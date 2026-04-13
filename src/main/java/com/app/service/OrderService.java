package com.app.service;

import com.app.dto.ApiResponse;
import com.app.dto.OrderListResponse;
import com.app.dto.OrderRequest;
import com.app.dto.OrderResponse;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest request);

    OrderListResponse getOrders(String userId);

    OrderResponse getOrderById(String orderId);

    ApiResponse cancelOrder(String orderId);

    ApiResponse updateStatus(String orderId, String status);

    OrderListResponse getByStatus(String status);

    OrderResponse trackOrder(String orderId);
}