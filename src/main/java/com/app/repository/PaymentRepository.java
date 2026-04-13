package com.app.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {

    List<Payment> findByOrderId(String orderId);

    List<Payment> findByStatus(String status);
}