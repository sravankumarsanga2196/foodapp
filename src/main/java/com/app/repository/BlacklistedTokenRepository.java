package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.app.entity.BlacklistedToken;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {

    boolean existsByToken(String token);
}