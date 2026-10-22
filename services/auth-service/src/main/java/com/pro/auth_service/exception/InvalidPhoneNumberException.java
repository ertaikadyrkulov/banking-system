package com.pro.auth_service.exception;

public class InvalidPhoneNumberException extends RuntimeException {

    public InvalidPhoneNumberException() {
        super("Invalid phone number");
    }

    public InvalidPhoneNumberException(
            Throwable cause
    ) {
        super("Invalid phone number", cause);
    }

}
