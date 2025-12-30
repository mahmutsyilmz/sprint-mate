package com.yilmaz.sprintmate.modules.auth.dto;

import com.yilmaz.sprintmate.modules.auth.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleSelectionRequest {
    @NotNull(message = "Role cannot be null")
    private UserRole role;
}
