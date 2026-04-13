package com.app.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtpResponse {
    private String status;
    private String message;
    private String otpId;
}