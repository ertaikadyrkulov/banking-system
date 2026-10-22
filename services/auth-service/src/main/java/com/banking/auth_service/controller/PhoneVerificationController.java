package com.banking.auth_service.controller;

import com.banking.auth_service.dto.request.PhoneVerificationRequest;
import com.banking.auth_service.dto.request.VerifyOtpRequest;
import com.banking.auth_service.dto.response.RequestOtpResponse;
import com.banking.auth_service.dto.response.VerifyOtpResponse;
import com.banking.auth_service.service.PhoneVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/phone-verification")
@RequiredArgsConstructor
public class PhoneVerificationController {

    private final PhoneVerificationService phoneVerificationService;

    @PostMapping("/request")
    public ResponseEntity<RequestOtpResponse> requestPhoneVerification(
            @RequestBody @Valid PhoneVerificationRequest request
    ) {
        return ResponseEntity
                .accepted()
                .body(phoneVerificationService.requestOtp(request));
    }

    @PostMapping("/verify")
    public ResponseEntity<VerifyOtpResponse> verifyPhoneVerification(
            @RequestBody @Valid VerifyOtpRequest request
    ) {
        return ResponseEntity
                .ok(phoneVerificationService.verifyOtp(request));
    }

}

