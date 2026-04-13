package com.app.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem, String> {

    Optional<CartItem> findByCartIdAndItemId(String cartId, String itemId);

    List<CartItem> findByCartId(String cartId);

    void deleteByCartIdAndItemId(String cartId, String itemId);

    void deleteByCartId(String cartId);
}