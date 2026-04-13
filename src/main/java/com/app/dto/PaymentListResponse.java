package com.app.dto;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentListResponse {

    private String status;
    private List<PaymentResponse> payments;
}