package com.banking.exception.handler;

import com.banking.exception.ApplicationException;
import com.banking.exception.model.CommonErrorCode;
import com.banking.exception.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(
            ApplicationException ex,
            HttpServletRequest request
    ) {

        if (ex.getCause() != null) {
            log.error(
                    "Application exception: code={}, method={}, path={}",
                    ex.getErrorCode(),
                    request.getMethod(),
                    request.getRequestURI(),
                    ex
            );
        }

        ErrorResponse body = ErrorResponse.of(
                ex.getErrorCode(),
                ex.getMessage(),
                request.getRequestURI(),
                ex.getDetails()
        );

        return ResponseEntity
                .status(ex.getErrorCode().getStatus())
                .body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse body = ErrorResponse.of(
                CommonErrorCode.VALIDATION_FAILED,
                message,
                request.getRequestURI()
        );

        return ResponseEntity
                .status(CommonErrorCode.VALIDATION_FAILED.getStatus())
                .body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception ex,
            HttpServletRequest request
    ) {

        log.error(
                "Unexpected exception: method={}, path={}",
                request.getMethod(),
                request.getRequestURI(),
                ex
        );

        ErrorResponse body = ErrorResponse.of(
                CommonErrorCode.INTERNAL_ERROR,
                CommonErrorCode.INTERNAL_ERROR.getDefaultMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(CommonErrorCode.INTERNAL_ERROR.getStatus())
                .body(body);
    }

}

