package com.app.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class GenerateOtpRequest {

    @Schema(example = "9876543210", description = "User phone number")
    private String phoneNumber;
}