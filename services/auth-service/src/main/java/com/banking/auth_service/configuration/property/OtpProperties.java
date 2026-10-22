package com.banking.auth_service.configuration.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.time.Duration;

@ConfigurationProperties(prefix = "application.otp")
public record OtpProperties(
        @DefaultValue("6") int length,
        @DefaultValue("5m") Duration ttl,
        @DefaultValue("5") int maxAttempts,
        @DefaultValue("60s") Duration resendCooldown,
        @DefaultValue("5") int maxSendsPerDay,
        @DefaultValue("15m") Duration lockDuration,
        @DefaultValue("10m") Duration verificationTokenTtl,
        String pepper
) {

    public OtpProperties {
        if (pepper == null || pepper.isBlank()) {
            throw new IllegalStateException("application.otp.pepper must be set (secret, taken from env/secret store)");
        }
        if (length < 4 || length > 9) {
            throw new IllegalStateException("application.otp.length must be between 4 and 9");
        }
    }

}
