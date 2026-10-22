package com.banking.exception;
import com.banking.exception.model.ErrorCode;
import lombok.Getter;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class ApplicationException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    public ApplicationException(ErrorCode errorCode) {
        this(errorCode, errorCode.getDefaultMessage(), null, Map.of());
    }

    public ApplicationException(
            ErrorCode errorCode,
            String message
    ) {
        this(errorCode, message, null, Map.of());
    }

    public ApplicationException(
            ErrorCode errorCode,
            Throwable cause
    ) {
        this(errorCode, errorCode.getDefaultMessage(), cause, Map.of());
    }

    public ApplicationException(
            ErrorCode errorCode,
            String message,
            Throwable cause
    ) {
        this(errorCode, message, cause, Map.of());
    }

    public ApplicationException(
            ErrorCode errorCode,
            String message,
            Map<String, Object> details
    ) {
        this(errorCode, message, null, details);
    }

    public ApplicationException(
            ErrorCode errorCode,
            String message,
            Throwable cause,
            Map<String, Object> details
    ) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details = details == null
                ? Map.of()
                : Collections.unmodifiableMap(new LinkedHashMap<>(details));
    }

}

