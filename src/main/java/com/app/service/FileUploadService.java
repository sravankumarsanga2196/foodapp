package com.app.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.app.exception.CustomException;
import com.app.entity.ErrorCode;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final Cloudinary cloudinary;

    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB

    public String uploadFile(MultipartFile file, String folder) {

        validateFile(file);

        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", folder)
            );

            return result.get("secure_url").toString();

        } catch (IOException e) {
            throw new CustomException(
                    ErrorCode.FILE_UPLOAD_FAILED,
                    "File upload failed"
            );
        }
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new CustomException(
                    ErrorCode.INVALID_FILE,
                    "File is empty"
            );
        }

        if (file.getSize() > MAX_SIZE) {
            throw new CustomException(
                    ErrorCode.FILE_TOO_LARGE,
                    "Max size is 5MB"
            );
        }

        String type = file.getContentType();

        if (type == null || (
                !type.equals("image/jpeg") &&
                !type.equals("image/png") &&
                !type.equals("image/jpg")
        )) {
            throw new CustomException(
                    ErrorCode.INVALID_FILE_TYPE,
                    "Only JPG, JPEG, PNG allowed"
            );
        }
    }
}