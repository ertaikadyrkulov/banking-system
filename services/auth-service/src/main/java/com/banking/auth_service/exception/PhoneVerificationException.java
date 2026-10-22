package com.banking.auth_service.exception;

import com.banking.auth_service.exception.model.Reason;
import com.banking.exception.ApplicationException;
import lombok.Getter;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Getter
public class PhoneVerificationException extends ApplicationException {

    private final Reason reason;
    private final Duration retryAfter;
    private final Integer attemptsLeft;

    public PhoneVerificationException(
            Reason reason,
            Duration retryAfter,
            Integer attemptsLeft,
            Throwable cause
    ) {
        super(
                reason.getErrorCode(),
                reason.getErrorCode().getDefaultMessage(),
                cause,
                createDetails(retryAfter, attemptsLeft)
        );

        this.reason = reason;
        this.retryAfter = retryAfter;
        this.attemptsLeft = attemptsLeft;
    }

    public static PhoneVerificationException of(Reason reason) {
        return new PhoneVerificationException(reason, null, null, null);
    }

    public static PhoneVerificationException causedBy(
            Reason reason,
            Throwable cause
    ) {
        return new PhoneVerificationException(reason, null, null, cause);
    }

    public static PhoneVerificationException retryAfter(
            Reason reason,
            Duration retryAfter
    ) {
        return new PhoneVerificationException(
                reason, retryAfter, null, null
        );
    }

    public static PhoneVerificationException invalidCode(int attemptsLeft) {
        return new PhoneVerificationException(
                Reason.INVALID_CODE, null, attemptsLeft, null
        );
    }

    private static Map<String, Object> createDetails(
            Duration retryAfter,
            Integer attemptsLeft
    ) {
        Map<String, Object> details = new HashMap<>();

        if (retryAfter != null) {
            details.put("retryAfterSeconds", retryAfter.toSeconds());
        }

        if (attemptsLeft != null) {
            details.put("attemptsLeft", attemptsLeft);
        }

        return details;
    }

}
