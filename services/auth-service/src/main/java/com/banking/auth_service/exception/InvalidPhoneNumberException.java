package com.banking.auth_service.exception;

import com.banking.auth_service.exception.model.AuthErrorCode;
import com.banking.exception.ValidationException;

public class InvalidPhoneNumberException extends ValidationException {

    public InvalidPhoneNumberException(String message) {
        super(AuthErrorCode.INVALID_PHONE_NUMBER, message);
    }

    public InvalidPhoneNumberException() {
        super(AuthErrorCode.INVALID_PHONE_NUMBER);
    }

    public InvalidPhoneNumberException(Throwable cause) {
        super(AuthErrorCode.INVALID_PHONE_NUMBER, cause);
    }

}
