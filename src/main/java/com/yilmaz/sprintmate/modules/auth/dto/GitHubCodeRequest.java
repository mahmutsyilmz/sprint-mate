package com.yilmaz.sprintmate.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * GitHub OAuth Code Request
 * 
 * Frontend receives authorization code from GitHub and sends it to this endpoint
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubCodeRequest {

    @NotBlank(message = "Authorization code is required")
    private String code;

    // Optional: Frontend's redirect URI (for validation)
    private String redirectUri;
}
