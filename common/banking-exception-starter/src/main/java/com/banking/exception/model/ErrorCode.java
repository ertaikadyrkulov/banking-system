package com.banking.exception.model;

import org.springframework.http.HttpStatus;

public interface ErrorCode {

    HttpStatus getStatus();
    String getDefaultMessage();
    String name();

}
