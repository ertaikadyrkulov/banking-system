package com.banking.auth_service.util;

public record PhoneNumberData(
        String phoneCountryCode,
        String phoneNumber,
        String internationalPhone,
        String nationalPhoneNumber
) {}
