package com.pro.auth_service.util;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.pro.auth_service.exception.InvalidPhoneNumberException;

import java.util.regex.Pattern;

public final class PhoneUtil {

    private static final PhoneNumberUtil PHONE_UTIL = PhoneNumberUtil.getInstance();
    private static final Pattern ALLOWED_INPUT  = Pattern.compile("^[0-9]{4,20}$");

    private PhoneUtil() {}

    public static PhoneNumberData validatePhoneNumber(String phoneCountryCode, String phoneNumber) {
        if (phoneCountryCode == null || phoneCountryCode.isBlank()
                || phoneNumber == null || phoneNumber.isBlank()) {
            throw new InvalidPhoneNumberException();
        }

        String countryCode = phoneCountryCode.trim().replaceFirst("^\\+", "");
        if (!countryCode.matches("\\d{1,3}")) {
            throw new InvalidPhoneNumberException();
        }

        String number = phoneNumber.trim();
        if (!ALLOWED_INPUT.matcher(number).matches()) {
            throw new InvalidPhoneNumberException();
        }

        try {
            Phonenumber.PhoneNumber parsed =
                    PHONE_UTIL.parse("+" + countryCode + number, null);

            if (!PHONE_UTIL.isValidNumber(parsed)) {
                throw new InvalidPhoneNumberException();
            }

            String e164 = PHONE_UTIL.format(parsed, PhoneNumberUtil.PhoneNumberFormat.E164);

            return new PhoneNumberData(
                    String.valueOf(parsed.getCountryCode()),
                    PHONE_UTIL.getNationalSignificantNumber(parsed),
                    e164,
                    e164.substring(1)
            );
        } catch (NumberParseException ex) {
            throw new InvalidPhoneNumberException(ex);
        }
    }

}
