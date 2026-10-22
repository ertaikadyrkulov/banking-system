package com.banking.auth_service.util;

import com.banking.auth_service.exception.CryptographyException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HexFormat;

public final class AuthUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generateCode(int length) {
        int bound = (int) Math.pow(10, length);
        return String.format("%0" + length + "d", RANDOM.nextInt(bound));
    }

    public static String hash(String phone, String code, String pepper) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(pepper.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return HexFormat.of().formatHex(mac.doFinal((phone + ":" + code).getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException e) {
            throw new CryptographyException("Failed to calculate OTP HMAC", e);
        }
    }

    public static String generateToken() {
        byte[] bytes = new byte[32];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    public static String codeKey(String phone) {
        return "otp:code:" + phone;
    }

    public static String cooldownKey(String phone) {
        return "otp:cooldown:" + phone;
    }

    public static String dailyKey(String phone) {
        return "otp:daily:" + phone;
    }

    public static String lockKey(String phone) {
        return "otp:lock:" + phone;
    }

    public static String tokenKey(String token) {
        return "otp:vtoken:" + token;
    }

}

