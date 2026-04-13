package com.app.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, String> {

    Optional<Cart> findByUserIdAndIsActiveTrue(String userId);
}