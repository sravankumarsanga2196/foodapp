package com.app.dto;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderListResponse {

    private String status;
    private List<OrderResponse> orders;
}