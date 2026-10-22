package com.banking.exception;

import com.banking.exception.model.ErrorCode;

public class NotFoundException extends ApplicationException {

    public NotFoundException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }

}
