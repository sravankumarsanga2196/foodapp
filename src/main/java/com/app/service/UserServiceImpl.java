package com.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.dto.*;
import com.app.entity.BlacklistedToken;
import com.app.entity.ErrorCode;
import com.app.entity.User;
import com.app.exception.CustomException;
import com.app.repository.BlacklistedTokenRepository;
import com.app.repository.UserRepository;
import com.app.security.JwtUtil;
import com.app.util.OtpUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OtpUtil otpUtil;
    private final JwtUtil jwtUtil;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final FileUploadService fileUploadService;

    // ========================= OTP GENERATION =========================
    @Override
    public OtpResponse generateOtp(GenerateOtpRequest request) {

        validatePhoneNumber(request.getPhoneNumber());

        String otp = otpUtil.generateOtp();

        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElse(User.builder()
                        .phoneNumber(request.getPhoneNumber())
                        .isVerified(false)
                        .build());

        user.setOtp(otp);
        user.setOtpExpiry(System.currentTimeMillis() + 5 * 60 * 1000);

        userRepository.save(user);

        System.out.println("Generated OTP: " + otp);

        return OtpResponse.builder()
                .status("SUCCESS")
                .message("OTP generated successfully")
                .otpId("OTP_" + user.getId())
                .build();
    }

    // ========================= OTP VERIFICATION =========================
    @Override
    public LoginResponse verifyOtp(VerifyOtpRequest request) {

        User user = userRepository.findByPhoneNumber(request.getPhoneNumber())
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found"
                ));

        validateOtp(user, request.getOtp());

        user.setVerified(true);
        user.setOtp(null);
        user.setOtpExpiry(null);

        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getId());

        return LoginResponse.builder()
                .status("SUCCESS")
                .message("Login successful")
                .userId(user.getId())
                .token(token)
                .build();
    }
    public ApiResponse updateProfile(UpdateProfileRequest request) {

        // ================= FIND USER =================
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new CustomException(
                        ErrorCode.USER_NOT_FOUND,
                        "User not found"
                ));

        // ================= UPDATE INFO =================
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        // ================= UPLOAD IMAGE =================
        if (request.getProfilePhoto() != null &&
                !request.getProfilePhoto().isEmpty()) {

            String imageUrl = fileUploadService.uploadFile(
                    request.getProfilePhoto(),
                    "users"
            );

            user.setProfilePhotoUrl(imageUrl);
        }

        // ================= SAVE =================
        userRepository.save(user);

        return ApiResponse.builder()
                .status("SUCCESS")
                .message("Profile updated successfully")
                .build();
    }


    // ========================= LOGOUT =========================
    @Override
    public ApiResponse logout(LogoutRequest request) {

        if (request.getToken() == null || request.getToken().isBlank()) {
            throw new CustomException(
                    ErrorCode.INVALID_TOKEN,
                    "Token is required"
            );
        }

        blacklistedTokenRepository.save(
                BlacklistedToken.builder()
                        .token(request.getToken())
                        .userId(request.getUserId())
                        .build()
        );

        return ApiResponse.builder()
                .status("SUCCESS")
                .message("Logged out successfully")
                .build();
    }

    // ========================= PRIVATE HELPERS =========================

    private void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() != 10) {
            throw new CustomException(
                    ErrorCode.INVALID_PHONE_NUMBER,
                    "Invalid phone number"
            );
        }
    }

    private void validateOtp(User user, String inputOtp) {

        if (user.getOtp() == null || !user.getOtp().equals(inputOtp)) {
            throw new CustomException(
                    ErrorCode.INVALID_OTP,
                    "Invalid OTP"
            );
        }

        if (user.getOtpExpiry() == null || user.getOtpExpiry() < System.currentTimeMillis()) {
            throw new CustomException(
                    ErrorCode.OTP_EXPIRED,
                    "OTP expired"
            );
        }
    }
}