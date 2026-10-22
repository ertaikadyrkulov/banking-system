package com.banking.exception.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ErrorResponse(
        String code,
        String message,
        Instant timestamp,
        String path,
        Map<String, Object> details
) {

    public static ErrorResponse of(
            ErrorCode errorCode,
            String message,
            String path,
            Map<String, Object> details
    ) {
        return new ErrorResponse(
                errorCode.name(),
                message,
                Instant.now(),
                path,
                details == null ? Map.of() : details
        );
    }

    public static ErrorResponse of(
            ErrorCode errorCode,
            String message,
            String path
    ) {
        return of(
                errorCode,
                message,
                path,
                Map.of()
        );
    }

}
