package com.yilmaz.sprintmate.modules.auth.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * GitHub OAuth Configuration Properties
 * 
 * Type-safe configuration - Preferred over @Value
 * Validation catches config errors at startup
 */
@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "github.client")
public class GitHubProperties {

    @NotBlank(message = "GitHub client ID is required")
    private String id;

    @NotBlank(message = "GitHub client secret is required")
    private String secret;

    // GitHub API endpoints - constant values
    private String tokenUrl = "https://github.com/login/oauth/access_token";
    private String userApiUrl = "https://api.github.com/user";
}
