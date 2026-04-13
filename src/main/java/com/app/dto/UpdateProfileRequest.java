package com.app.dto;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    private String userId;
    private String name;
    private String email;
    // ✅ Only for request layer
    private MultipartFile profilePhoto;
}