package com.banking.exception;

import com.banking.exception.model.ErrorCode;

public class ValidationException extends ApplicationException {

    public ValidationException(
            ErrorCode errorCode,
            String message,
            Throwable cause
    ) {
        super(errorCode, message, cause);
    }

    public ValidationException(
            ErrorCode errorCode,
            String message
    ) {
        super(errorCode, message);
    }

    public ValidationException(
            ErrorCode errorCode,
            Throwable cause
    ) {
        super(errorCode, cause);
    }

    public ValidationException(
            ErrorCode errorCode
    ) {
        super(errorCode);
    }

}
