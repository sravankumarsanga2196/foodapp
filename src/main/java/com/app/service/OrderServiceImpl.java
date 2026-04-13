package com.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.app.dto.ApiResponse;
import com.app.dto.OrderListResponse;
import com.app.dto.OrderRequest;
import com.app.dto.OrderResponse;
import com.app.entity.Cart;
import com.app.entity.CartItem;
import com.app.entity.ErrorCode;
import com.app.entity.Order;
import com.app.entity.OrderItem;
import com.app.exception.CustomException;
import com.app.repository.CartItemRepository;
import com.app.repository.CartRepository;
import com.app.repository.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepo;
    private final CartRepository cartRepo;
    private final CartItemRepository cartItemRepo;

    @Override
    public OrderResponse placeOrder(OrderRequest request) {

        Cart cart = cartRepo.findByUserIdAndIsActiveTrue(request.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.CART_EMPTY, "Cart is empty"));

        List<CartItem> cartItems = cartItemRepo.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new CustomException(ErrorCode.CART_EMPTY, "Cart is empty");
        }

        Order order = Order.builder()
                .userId(request.getUserId())
                .address(request.getAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .paymentMethod(request.getPaymentMethod())
                .status("PLACED")
                .createdAt(LocalDateTime.now())
                .totalAmount(cart.getTotalAmount())
                .build();

        List<OrderItem> orderItems = cartItems.stream()
                .map(c -> OrderItem.builder()
                        .itemId(c.getItemId())
                        .name(c.getName())
                        .price(c.getPrice())
                        .quantity(c.getQuantity())
                        .order(order)
                        .build())
                .toList();

        order.setItems(orderItems);

        orderRepo.save(order);

        // 🔥 Clear cart after order
        cartItemRepo.deleteByCartId(cart.getId());
        cart.setTotalAmount(0.0);
        cartRepo.save(cart);

        return OrderResponse.builder()
                .orderId(order.getId())
                .status("SUCCESS")
                .totalAmount(order.getTotalAmount())
                .message("Order placed successfully")
                .build();
    }

    @Override
    public OrderListResponse getOrders(String userId) {

        List<Order> orders = orderRepo.findByUserIdOrderByCreatedAtDesc(userId);

        return OrderListResponse.builder()
                .status("SUCCESS")
                .orders(orders.stream().map(this::mapToResponse).toList())
                .build();
    }

    @Override
    public OrderResponse getOrderById(String orderId) {

        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND, "Order not found"));

        return mapToResponse(order);
    }

    @Override
    public ApiResponse cancelOrder(String orderId) {

        Order order = get(orderId);

        if (!order.getStatus().equals("PLACED")) {
            throw new CustomException(ErrorCode.INVALID_ORDER_STATE, "Cannot cancel this order");
        }

        order.setStatus("CANCELLED");
        orderRepo.save(order);

        return success("Order cancelled");
    }

    @Override
    public ApiResponse updateStatus(String orderId, String status) {

        Order order = get(orderId);

        order.setStatus(status);
        orderRepo.save(order);

        return success("Status updated");
    }

    @Override
    public OrderListResponse getByStatus(String status) {

        List<Order> list = orderRepo.findByStatus(status);

        return OrderListResponse.builder()
                .status("SUCCESS")
                .orders(list.stream().map(this::mapToResponse).toList())
                .build();
    }

    @Override
    public OrderResponse trackOrder(String orderId) {

        Order order = get(orderId);

        return mapToResponse(order);
    }

    // 🔥 Helpers

    private Order get(String id) {
        return orderRepo.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND, "Order not found"));
    }

    private OrderResponse mapToResponse(Order order) {

        return OrderResponse.builder()
                .orderId(order.getId())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .message("Order fetched")
                .build();
    }

    private ApiResponse success(String msg) {
        return ApiResponse.builder()
                .status("SUCCESS")
                .message(msg)
                .build();
    }
}