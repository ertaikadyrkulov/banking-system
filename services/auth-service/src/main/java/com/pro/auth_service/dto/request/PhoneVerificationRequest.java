package com.pro.auth_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PhoneVerificationRequest(

        @NotBlank(message = "Phone country code cannot be blank")
        @Pattern(
                regexp = "\\+?\\d{1,3}",
                message = "Phone country code must contain 1 to 3 digits and an optional leading '+'"
        )
        String phoneCountryCode,

        @NotBlank(message = "Phone number cannot be blank")
        @Pattern(
                regexp = "[0-9]+",
                message = "Phone number must contain digits only"
        )
        String phoneNumber

) {}
