package com.yilmaz.sprintmate.modules.auth.exception;

import com.yilmaz.sprintmate.common.exception.BaseException;
import org.springframework.http.HttpStatus;

/**
 * Authentication Exception
 * 
 * For GitHub OAuth or JWT related errors
 */
public class AuthenticationException extends BaseException {

    public AuthenticationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "AUTH_ERROR");
    }

    public AuthenticationException(String message, String errorCode) {
        super(message, HttpStatus.UNAUTHORIZED, errorCode);
    }

    public static AuthenticationException invalidGitHubCode() {
        return new AuthenticationException(
                "Invalid or expired GitHub authorization code",
                "INVALID_GITHUB_CODE");
    }

    public static AuthenticationException githubApiError(String details) {
        return new AuthenticationException(
                "Failed to authenticate with GitHub: " + details,
                "GITHUB_API_ERROR");
    }

    public static AuthenticationException invalidToken() {
        return new AuthenticationException(
                "Invalid or expired token",
                "INVALID_TOKEN");
    }

    public static AuthenticationException tokenExpired() {
        return new AuthenticationException(
                "Token has expired",
                "TOKEN_EXPIRED");
    }
}
