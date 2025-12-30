package com.yilmaz.sprintmate.modules.auth.controller;

import com.yilmaz.sprintmate.common.dto.ApiResponse;
import com.yilmaz.sprintmate.modules.auth.constant.AuthConstants;
import com.yilmaz.sprintmate.modules.auth.dto.AuthResponse;
import com.yilmaz.sprintmate.modules.auth.dto.GitHubCodeRequest;
import com.yilmaz.sprintmate.modules.auth.dto.RoleSelectionRequest;
import com.yilmaz.sprintmate.modules.auth.dto.UserDto;
import com.yilmaz.sprintmate.modules.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.yilmaz.sprintmate.modules.auth.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Auth Controller
 * 
 * Authentication endpoints:
 * - POST /api/auth/github - GitHub OAuth login
 * - POST /api/auth/role - Set user role
 * - GET /api/auth/me - Get current user
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "GitHub OAuth and user management")
public class AuthController {

        private final AuthService authService;

        /**
         * Login with GitHub OAuth
         * 
         * Frontend sends the code received from GitHub to this endpoint
         * Backend returns JWT token
         */
        @PostMapping("/github")
        @Operation(summary = "GitHub OAuth Login", description = "Authenticate with GitHub authorization code and get JWT token")
        @ApiResponses(value = {
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Authentication successful", content = @Content(schema = @Schema(implementation = AuthResponse.class))),
                        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Invalid GitHub code")
        })
        public ResponseEntity<ApiResponse<AuthResponse>> authenticateWithGitHub(
                        @Valid @RequestBody GitHubCodeRequest request) {

                log.info("GitHub authentication request received");

                AuthResponse authResponse = authService.authenticateWithGitHub(request.getCode());

                String message = authResponse.isNewUser()
                                ? AuthConstants.MSG_WELCOME_NEW_USER
                                : AuthConstants.MSG_WELCOME_BACK;

                return ResponseEntity.ok(ApiResponse.success(authResponse, message));
        }

        /**
         * Select/update user role
         * 
         * New users must select role on first login: FRONTEND or BACKEND
         */
        @PostMapping("/role")
        @Operation(summary = "Set User Role", description = "Select user role (FRONTEND or BACKEND)")
        @SecurityRequirement(name = "bearerAuth")
        public ResponseEntity<ApiResponse<UserDto>> selectRole(
                        @AuthenticationPrincipal User user,
                        @Valid @RequestBody RoleSelectionRequest request) {

                log.info("Role selection request: {}", request.getRole());

                UserDto updatedUser = authService.updateUserRole(
                                user.getId(),
                                request.getRole());

                return ResponseEntity.ok(ApiResponse.success(updatedUser, AuthConstants.MSG_ROLE_UPDATED));
        }

        /**
         * Get current user info
         */
        @GetMapping("/me")
        @Operation(summary = "Get Current User", description = "Get current user info from JWT token")
        @SecurityRequirement(name = "bearerAuth")
        public ResponseEntity<ApiResponse<UserDto>> getCurrentUser(
                        @AuthenticationPrincipal User user) {

                return ResponseEntity.ok(ApiResponse.success(UserDto.fromEntity(user)));
        }
}
