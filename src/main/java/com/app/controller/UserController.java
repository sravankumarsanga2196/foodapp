package com.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.app.dto.*;
import com.app.service.UserService;

import lombok.RequiredArgsConstructor;
@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // ✅ Generate OTP
    @PostMapping("/generate-otp")
    public ResponseEntity<OtpResponse> generateOtp(@RequestBody GenerateOtpRequest request) {
        return ResponseEntity.ok(userService.generateOtp(request));
    }

    // ✅ Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<LoginResponse> verifyOtp(@RequestBody VerifyOtpRequest request) {
        return ResponseEntity.ok(userService.verifyOtp(request));
    }
 // ✅ Swagger will show file upload field
    @PutMapping(value = "/update-profile", consumes = "multipart/form-data")
    public ApiResponse updateProfile(@ModelAttribute UpdateProfileRequest request) {
        return userService.updateProfile(request);
    }
   
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestBody LogoutRequest request) {
        return ResponseEntity.ok(userService.logout(request));
    }
}