package com.app.service;

import com.app.dto.ApiResponse;
import com.app.dto.GenerateOtpRequest;
import com.app.dto.LoginResponse;
import com.app.dto.LogoutRequest;
import com.app.dto.OtpResponse;
import com.app.dto.UpdateProfileRequest;
import com.app.dto.VerifyOtpRequest;

public interface UserService {

    OtpResponse generateOtp(GenerateOtpRequest request);

    LoginResponse verifyOtp(VerifyOtpRequest request);

    ApiResponse updateProfile(UpdateProfileRequest request);
    ApiResponse logout(LogoutRequest request);
}