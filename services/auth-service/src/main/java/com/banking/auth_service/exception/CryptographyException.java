package com.banking.auth_service.exception;

import com.banking.exception.ApplicationException;
import com.banking.exception.model.CommonErrorCode;
import com.banking.exception.model.ErrorCode;

public class CryptographyException extends ApplicationException {

    public CryptographyException(
            ErrorCode errorCode,
            String message,
            Throwable cause
    ) {
        super(errorCode, message, cause, null);
    }

    public CryptographyException(
            String message,
            Throwable cause
    ) {
        super(CommonErrorCode.INTERNAL_ERROR, message, cause, null);
    }

}
