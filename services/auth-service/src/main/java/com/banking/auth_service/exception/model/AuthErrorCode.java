package com.banking.auth_service.exception.model;

import com.banking.exception.model.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum AuthErrorCode implements ErrorCode {

    // otp

    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "Invalid verification code"),
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST,"Verification code expired"),
    TOO_MANY_VERIFICATION_ATTEMPTS(HttpStatus.TOO_MANY_REQUESTS, "Too many attempts. Please try again later."),
    VERIFICATION_LOCKED(HttpStatus.LOCKED, "Verification is locked. Please contact support."),
    VERIFICATION_RESEND_COOLDOWN(HttpStatus.TOO_MANY_REQUESTS, "Please wait before resending the code."),
    VERIFICATION_DAILY_LIMIT_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "Daily limit exceeded. Please try again later."),
    INVALID_VERIFICATION_TOKEN(HttpStatus.BAD_REQUEST, "Invalid verification token"),

    // phone

    PHONE_ALREADY_REGISTERED(HttpStatus.BAD_REQUEST, "Phone number already registered"),
    INVALID_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "Invalid phone number");

    private final HttpStatus status;
    private final String defaultMessage;

}
