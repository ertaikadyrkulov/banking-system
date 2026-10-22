package com.pro.auth_service.exception.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Reason {

    INVALID_CODE(AuthErrorCode.INVALID_VERIFICATION_CODE),
    CODE_EXPIRED(AuthErrorCode.VERIFICATION_CODE_EXPIRED),
    TOO_MANY_ATTEMPTS(AuthErrorCode.TOO_MANY_VERIFICATION_ATTEMPTS),
    LOCKED(AuthErrorCode.VERIFICATION_LOCKED),
    RESEND_COOLDOWN(AuthErrorCode.VERIFICATION_RESEND_COOLDOWN),
    DAILY_LIMIT_EXCEEDED(AuthErrorCode.VERIFICATION_DAILY_LIMIT_EXCEEDED),
    INVALID_VERIFICATION_TOKEN(AuthErrorCode.INVALID_VERIFICATION_TOKEN);

    private final AuthErrorCode errorCode;

}
