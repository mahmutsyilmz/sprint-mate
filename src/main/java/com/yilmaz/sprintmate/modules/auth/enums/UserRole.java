package com.yilmaz.sprintmate.modules.auth.enums;

import lombok.Getter;

/**
 * User Roles
 * 
 * Defines the role and specialization of a user in the system.
 */
@Getter
public enum UserRole {
    FRONTEND("Frontend Developer"),
    BACKEND("Backend Developer"),
    PRODUCT_OWNER("Product Owner"),
    FULL_STACK("Full Stack Developer"),
    DESIGNER("UI/UX Designer"),
    QA("Quality Assurance");

    private final String displayName;

    UserRole(String displayName) {
        this.displayName = displayName;
    }
}
