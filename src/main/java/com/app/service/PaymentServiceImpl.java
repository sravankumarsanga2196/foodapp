package com.app.service;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.app.dto.ApiResponse;
import com.app.dto.PaymentCallbackRequest;
import com.app.dto.PaymentListResponse;
import com.app.dto.PaymentRequest;
import com.app.dto.PaymentResponse;
import com.app.entity.ErrorCode;
import com.app.entity.Order;
import com.app.entity.Payment;
import com.app.exception.CustomException;
import com.app.repository.OrderRepository;
import com.app.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repo;
    private final OrderRepository orderRepo;

    @Override
    public PaymentResponse initiatePayment(PaymentRequest request) {

        Order order = orderRepo.findById(request.getOrderId())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND, "Order not found"));

        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .status("INITIATED")
                .paymentUrl("https://upi-payment-link") // simulate
                .createdAt(LocalDateTime.now())
                .build();

        repo.save(payment);

        return PaymentResponse.builder()
                .status("SUCCESS")
                .paymentId(payment.getId())
                .paymentUrl(payment.getPaymentUrl())
                .message("Payment initiated")
                .build();
    }

    @Override
    public ApiResponse handleCallback(PaymentCallbackRequest request) {

        Payment payment = repo.findById(request.getPaymentId())
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND, "Payment not found"));

        payment.setStatus(request.getStatus());
        payment.setTransactionId("TXN_" + System.currentTimeMillis());

        repo.save(payment);

        // 🔥 Update Order Status
        Order order = orderRepo.findById(payment.getOrderId())
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND, "Order not found"));

        if (request.getStatus().equals("SUCCESS")) {
            order.setStatus("CONFIRMED");
        } else {
            order.setStatus("FAILED");
        }

        orderRepo.save(order);

        return ApiResponse.builder()
                .status("SUCCESS")
                .message("Payment status updated")
                .build();
    }

    @Override
    public PaymentResponse getPayment(String paymentId) {

        Payment p = repo.findById(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND, "Payment not found"));

        return map(p);
    }

    @Override
    public PaymentListResponse getByOrder(String orderId) {

        List<Payment> list = repo.findByOrderId(orderId);

        return mapList(list);
    }

    @Override
    public PaymentListResponse getByStatus(String status) {

        List<Payment> list = repo.findByStatus(status);

        return mapList(list);
    }

    @Override
    public ApiResponse retryPayment(String paymentId) {

        Payment p = repo.findById(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND, "Payment not found"));

        p.setStatus("INITIATED");
        repo.save(p);

        return success("Payment retried");
    }

    @Override
    public ApiResponse cancelPayment(String paymentId) {

        Payment p = repo.findById(paymentId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND, "Payment not found"));

        p.setStatus("CANCELLED");
        repo.save(p);

        return success("Payment cancelled");
    }

    // 🔥 Helpers

    private PaymentResponse map(Payment p) {
        return PaymentResponse.builder()
                .status(p.getStatus())
                .paymentId(p.getId())
                .paymentUrl(p.getPaymentUrl())
                .message("Fetched")
                .build();
    }

    private PaymentListResponse mapList(List<Payment> list) {
        return PaymentListResponse.builder()
                .status("SUCCESS")
                .payments(list.stream().map(this::map).toList())
                .build();
    }

    private ApiResponse success(String msg) {
        return ApiResponse.builder()
                .status("SUCCESS")
                .message(msg)
                .build();
    }
}