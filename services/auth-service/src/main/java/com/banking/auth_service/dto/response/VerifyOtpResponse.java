package com.banking.auth_service.dto.response;

public record VerifyOtpResponse(

        String verificationToken,
        Long expiresInSeconds

) {}
