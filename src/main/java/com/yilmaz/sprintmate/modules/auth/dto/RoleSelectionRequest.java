package com.yilmaz.sprintmate.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Role Selection Request
 * 
 * User selects role on first login: FRONTEND or BACKEND
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleSelectionRequest {

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(FRONTEND|BACKEND)$", message = "Role must be FRONTEND or BACKEND")
    private String role;
}
