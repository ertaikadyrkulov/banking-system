package com.banking.auth_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record VerifyOtpRequest(

        @NotBlank(message = "Phone country code cannot be blank")
        String phoneCountryCode,

        @NotBlank(message = "Phone number cannot be blank")
        String phoneNumber,

        @NotBlank(message = "Code cannot be blank")
        @Pattern(regexp = "^\\d{6}$", message = "code must be digits")
        String code

) {}
