package com.app.service;

import com.app.dto.ApiResponse;
import com.app.dto.PaymentCallbackRequest;
import com.app.dto.PaymentListResponse;
import com.app.dto.PaymentRequest;
import com.app.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse initiatePayment(PaymentRequest request);

    ApiResponse handleCallback(PaymentCallbackRequest request);

    PaymentResponse getPayment(String paymentId);

    PaymentListResponse getByOrder(String orderId);

    PaymentListResponse getByStatus(String status);

    ApiResponse retryPayment(String paymentId);

    ApiResponse cancelPayment(String paymentId);
}