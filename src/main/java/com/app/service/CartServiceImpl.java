package com.app.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.app.dto.AddToCartRequest;
import com.app.dto.ApiResponse;
import com.app.dto.CartItemResponse;
import com.app.dto.CartResponse;
import com.app.dto.RemoveCartRequest;
import com.app.dto.UpdateQuantityRequest;
import com.app.entity.Cart;
import com.app.entity.CartItem;
import com.app.entity.ErrorCode;
import com.app.exception.CustomException;
import com.app.repository.CartItemRepository;
import com.app.repository.CartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepo;
    private final CartItemRepository itemRepo;

    @Override
    public ApiResponse addToCart(AddToCartRequest request) {

        Cart cart = cartRepo.findByUserIdAndIsActiveTrue(request.getUserId())
                .orElseGet(() -> cartRepo.save(
                        Cart.builder()
                                .userId(request.getUserId())
                                .totalAmount(0.0)
                                .isActive(true)
                                .build()
                ));

        CartItem item = itemRepo.findByCartIdAndItemId(cart.getId(), request.getItemId())
                .orElse(null);

        if (item != null) {
            item.setQuantity(item.getQuantity() + request.getQuantity());
        } else {
            item = CartItem.builder()
                    .itemId(request.getItemId())
                    .name("Chicken Biryani") // fetch from Menu service
                    .price(250.0)
                    .quantity(request.getQuantity())
                    .cart(cart)
                    .build();
        }

        itemRepo.save(item);
        updateTotal(cart);

        return ApiResponse.builder()
                .status("SUCCESS")
                .message("Item added to cart")
                .build();
    }

    @Override
    public CartResponse viewCart(String userId) {

        Cart cart = cartRepo.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_EMPTY, "Cart is empty"));

        List<CartItem> items = itemRepo.findByCartId(cart.getId());

        return CartResponse.builder()
                .status("SUCCESS")
                .cartItems(items.stream().map(i -> CartItemResponse.builder()
                        .itemId(i.getItemId())
                        .name(i.getName())
                        .price(i.getPrice())
                        .quantity(i.getQuantity())
                        .build()).toList())
                .totalAmount(cart.getTotalAmount())
                .build();
    }

    @Override
    public ApiResponse removeItem(RemoveCartRequest request) {

        Cart cart = getCart(request.getUserId());

        itemRepo.deleteByCartIdAndItemId(cart.getId(), request.getItemId());

        updateTotal(cart);

        return success("Item removed");
    }

    @Override
    public ApiResponse updateQuantity(UpdateQuantityRequest request) {

        Cart cart = getCart(request.getUserId());

        CartItem item = itemRepo.findByCartIdAndItemId(cart.getId(), request.getItemId())
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_ITEM_NOT_FOUND, "Item not found"));

        item.setQuantity(request.getQuantity());

        itemRepo.save(item);
        updateTotal(cart);

        return success("Quantity updated");
    }

    @Override
    public ApiResponse increaseQuantity(UpdateQuantityRequest request) {

        Cart cart = getCart(request.getUserId());

        CartItem item = itemRepo.findByCartIdAndItemId(cart.getId(), request.getItemId())
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_ITEM_NOT_FOUND, "Item not found"));

        item.setQuantity(item.getQuantity() + 1);

        itemRepo.save(item);
        updateTotal(cart);

        return success("Quantity increased");
    }

    @Override
    public ApiResponse decreaseQuantity(UpdateQuantityRequest request) {

        Cart cart = getCart(request.getUserId());

        CartItem item = itemRepo.findByCartIdAndItemId(cart.getId(), request.getItemId())
                .orElseThrow(() -> new CustomException(ErrorCode.FOOD_ITEM_NOT_FOUND, "Item not found"));

        if (item.getQuantity() <= 1) {
            itemRepo.delete(item);
        } else {
            item.setQuantity(item.getQuantity() - 1);
            itemRepo.save(item);
        }

        updateTotal(cart);

        return success("Quantity decreased");
    }

    @Override
    public ApiResponse clearCart(String userId) {

        Cart cart = getCart(userId);

        itemRepo.deleteByCartId(cart.getId());

        cart.setTotalAmount(0.0);
        cartRepo.save(cart);

        return success("Cart cleared");
    }

    // 🔥 Helper Methods

    private Cart getCart(String userId) {
        return cartRepo.findByUserIdAndIsActiveTrue(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.CART_EMPTY, "Cart not found"));
    }

    private void updateTotal(Cart cart) {

        List<CartItem> items = itemRepo.findByCartId(cart.getId());

        double total = items.stream()
                .mapToDouble(i -> i.getPrice() * i.getQuantity())
                .sum();

        cart.setTotalAmount(total);
        cartRepo.save(cart);
    }

    private ApiResponse success(String msg) {
        return ApiResponse.builder()
                .status("SUCCESS")
                .message(msg)
                .build();
    }
}