package com.app.dto;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private String status;      // ERROR
    private String errorCode;   // INVALID_OTP
    private String message;     // Human readable
    private LocalDateTime timestamp;
}