package com.banking.exception.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "application.exception")
public record ApplicationExceptionProperties(
        @DefaultValue("true") boolean enabled
) {}
