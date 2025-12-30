package com.yilmaz.sprintmate.modules.auth.service;

import com.yilmaz.sprintmate.modules.auth.dto.AuthResponse;
import com.yilmaz.sprintmate.modules.auth.dto.UserDto;
import com.yilmaz.sprintmate.modules.auth.entity.User;
import com.yilmaz.sprintmate.modules.auth.enums.UserRole;
import com.yilmaz.sprintmate.modules.auth.exception.UserNotFoundException;
import com.yilmaz.sprintmate.modules.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Auth Service
 * 
 * Authentication and user management operations
 * 
 * Responsibilities:
 * - GitHub OAuth flow orchestration
 * - User creation/update
 * - JWT token generation
 * - Role management
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final GitHubOAuthService gitHubOAuthService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    /**
     * GitHub OAuth ile authenticate
     * 
     * @param code GitHub authorization code
     * @return AuthResponse with JWT token and user info
     */
    @Transactional
    public AuthResponse authenticateWithGitHub(String code) {
        log.info("Starting GitHub authentication flow");

        // 1. GitHub code -> access token
        String gitHubAccessToken = gitHubOAuthService.exchangeCodeForToken(code);

        // 2. Access token -> GitHub user info
        GitHubOAuthService.GitHubUser gitHubUser = gitHubOAuthService.getGitHubUser(gitHubAccessToken);

        // 3. Find or create user
        boolean isNewUser = !userRepository.existsByGithubId(gitHubUser.getId().toString());
        User user = findOrCreateUser(gitHubUser);

        // 4. Update last login
        user.setLastLoginAt(LocalDateTime.now());
        user = userRepository.save(user);

        // 5. Generate JWT token
        String jwtToken = jwtService.generateToken(user);

        log.info("User authenticated successfully: {} (newUser: {})", user.getUsername(), isNewUser);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .tokenType("Bearer")
                .expiresIn(jwtService.getExpirationInSeconds())
                .user(UserDto.fromEntity(user))
                .newUser(isNewUser)
                .build();
    }

    /**
     * Update user role
     * 
     * @param userId User ID
     * @param role   New role (FRONTEND/BACKEND)
     * @return Updated user
     */
    @Transactional
    public UserDto updateUserRole(UUID userId, UserRole role) {
        log.info("Updating role for user {} to {}", userId, role);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.byId(userId.toString()));

        user.setRole(role);
        user = userRepository.save(user);

        log.info("Role updated successfully for user: {}", user.getUsername());
        return UserDto.fromEntity(user);
    }

    /**
     * Get user by ID
     */
    public UserDto getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> UserNotFoundException.byId(userId.toString()));
        return UserDto.fromEntity(user);
    }

    /**
     * Get user info from token
     */
    public UserDto getCurrentUser(String token) {
        UUID userId = jwtService.extractUserId(token);
        return getUserById(userId);
    }

    /**
     * Find or create GitHub user
     */
    private User findOrCreateUser(GitHubOAuthService.GitHubUser gitHubUser) {
        return userRepository.findByGithubId(gitHubUser.getId().toString())
                .map(existingUser -> updateExistingUser(existingUser, gitHubUser))
                .orElseGet(() -> createNewUser(gitHubUser));
    }

    /**
     * Update existing user (avatar, email may have changed)
     */
    private User updateExistingUser(User user, GitHubOAuthService.GitHubUser gitHubUser) {
        log.debug("Updating existing user: {}", user.getUsername());

        user.setUsername(gitHubUser.getLogin());
        user.setAvatarUrl(gitHubUser.getAvatarUrl());

        if (gitHubUser.getEmail() != null) {
            user.setEmail(gitHubUser.getEmail());
        }

        return user;
    }

    /**
     * Create new user
     */
    private User createNewUser(GitHubOAuthService.GitHubUser gitHubUser) {
        log.info("Creating new user from GitHub: {}", gitHubUser.getLogin());

        User newUser = User.builder()
                .githubId(gitHubUser.getId().toString())
                .username(gitHubUser.getLogin())
                .email(gitHubUser.getEmail())
                .avatarUrl(gitHubUser.getAvatarUrl())
                // Role is null - user will select later
                .xpPoints(0)
                .isActive(true)
                .build();

        return userRepository.save(newUser);
    }
}
