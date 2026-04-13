package com.app.dto;

import lombok.Data;

@Data
public class LogoutRequest {
    private Long userId;
    private String token;
}