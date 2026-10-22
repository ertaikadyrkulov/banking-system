package com.pro.auth_service.configuration.property;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationPropertiesScan(
        basePackages = {
                "com.pro.auth_service.configuration.property"
        })
public class ApplicationPropertyConfiguration {}
