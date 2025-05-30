package com.example.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class OtpUtils {

    private static final int OTP_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Generates a numeric OTP with a fixed length.
     */
    public static String generateOtp() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    /**
     Returns OTP expiry time (default 5 minutes from now).
     */
    public static String getOtpExpiryTime(int minutes) {
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(minutes);
        return expiryTime.format(formatter);
    }

    /**
     * Validates if the OTP is expired.
     */
    public static boolean isOtpExpired(String expiryTime) {
        LocalDateTime expiry = LocalDateTime.parse(expiryTime, formatter);
        return LocalDateTime.now().isAfter(expiry);
    }
}
