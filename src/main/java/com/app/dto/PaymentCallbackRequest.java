package com.app.dto;

import lombok.Data;

@Data
public class PaymentCallbackRequest {
    private String paymentId;
    private String status;
}