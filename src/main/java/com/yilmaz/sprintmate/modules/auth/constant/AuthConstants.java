package com.yilmaz.sprintmate.modules.auth.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Authentication Constants
 * 
 * Centralized place for auth-related constant values and messages.
 * Prevents magic strings in the codebase.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthConstants {

    // Messages
    public static final String MSG_WELCOME_NEW_USER = "Welcome to Sprint Mate! Please select your role.";
    public static final String MSG_WELCOME_BACK = "Welcome back!";
    public static final String MSG_ROLE_UPDATED = "Role updated successfully";
    public static final String MSG_LOGOUT_SUCCESS = "Logged out successfully";

    // Error Messages
    public static final String ERR_INVALID_TOKEN = "Invalid or expired token";
    public static final String ERR_USER_NOT_FOUND = "User not found";
    public static final String ERR_INVALID_ROLE = "Invalid role selected";
    public static final String ERR_GITHUB_AUTH_FAILED = "GitHub authentication failed";

    // Roles
    public static final String ROLE_FRONTEND = "FRONTEND";
    public static final String ROLE_BACKEND = "BACKEND";
}
