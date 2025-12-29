package com.yilmaz.sprintmate.modules.auth.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * JWT Configuration Properties
 * 
 * Type-safe configuration for JWT
 */
@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    @NotBlank(message = "JWT secret is required")
    private String secret;

    @Min(value = 60000, message = "JWT expiration must be at least 1 minute")
    private Long expiration = 86400000L; // Default: 24 hours

    private String issuer = "sprintmate";
}
