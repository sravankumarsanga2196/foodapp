package com.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    @Schema(example = "SUCCESS")
    private String status;

    @Schema(example = "Login successful")
    private String message;

    @Schema(example = "U123")
    private String userId;

    @Schema(example = "jwt-token")
    private String token;
}