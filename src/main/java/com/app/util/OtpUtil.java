package com.app.util;

import org.springframework.stereotype.Component;

@Component
public class OtpUtil {

    public String generateOtp() {
        return String.valueOf((int)(Math.random() * 9000) + 1000);
    }

    public long getExpiryTime() {
        return System.currentTimeMillis() + (5 * 60 * 1000); // 5 min
    }
}