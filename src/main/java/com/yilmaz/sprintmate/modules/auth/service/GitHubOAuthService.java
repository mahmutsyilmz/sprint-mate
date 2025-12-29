package com.yilmaz.sprintmate.modules.auth.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yilmaz.sprintmate.modules.auth.config.GitHubProperties;
import com.yilmaz.sprintmate.modules.auth.exception.AuthenticationException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * GitHub OAuth Service
 * 
 * GitHub OAuth authentication operations
 * 
 * Flow:
 * 1. Frontend redirects user to GitHub
 * 2. User authorizes on GitHub
 * 3. GitHub redirects to frontend with code
 * 4. Frontend sends code to this service
 * 5. This service exchanges code for access token
 * 6. Gets GitHub user info with access token
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GitHubOAuthService {

    private final GitHubProperties gitHubProperties;
    private final WebClient webClient;

    /**
     * Exchange GitHub authorization code for access token
     * 
     * @param code Authorization code from GitHub
     * @return GitHub access token
     */
    public String exchangeCodeForToken(String code) {
        log.debug("Exchanging GitHub code for access token");

        try {
            GitHubTokenResponse response = webClient.post()
                    .uri(gitHubProperties.getTokenUrl())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(new GitHubTokenRequest(
                            gitHubProperties.getId(),
                            gitHubProperties.getSecret(),
                            code))
                    .retrieve()
                    .bodyToMono(GitHubTokenResponse.class)
                    .block();

            if (response == null || response.getAccessToken() == null) {
                log.error("Failed to get access token from GitHub");
                throw AuthenticationException.invalidGitHubCode();
            }

            if (response.getError() != null) {
                log.error("GitHub OAuth error: {} - {}",
                        response.getError(), response.getErrorDescription());
                throw AuthenticationException.githubApiError(response.getErrorDescription());
            }

            log.debug("Successfully obtained GitHub access token");
            return response.getAccessToken();

        } catch (WebClientResponseException e) {
            log.error("GitHub API error: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw AuthenticationException.githubApiError(e.getMessage());
        }
    }

    /**
     * Get user info with GitHub access token
     * 
     * @param accessToken GitHub access token
     * @return GitHub user info
     */
    public GitHubUser getGitHubUser(String accessToken) {
        log.debug("Fetching GitHub user info");

        try {
            GitHubUser user = webClient.get()
                    .uri(gitHubProperties.getUserApiUrl())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .retrieve()
                    .bodyToMono(GitHubUser.class)
                    .block();

            if (user == null || user.getId() == null) {
                log.error("Failed to get user info from GitHub");
                throw AuthenticationException.githubApiError("Could not fetch user information");
            }

            log.debug("Successfully fetched GitHub user: {}", user.getLogin());
            return user;

        } catch (WebClientResponseException e) {
            log.error("GitHub API error while fetching user: {} - {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw AuthenticationException.githubApiError(e.getMessage());
        }
    }

    // ========== Inner DTOs for GitHub API ==========

    @Data
    private static class GitHubTokenRequest {
        @JsonProperty("client_id")
        private final String clientId;

        @JsonProperty("client_secret")
        private final String clientSecret;

        private final String code;
    }

    @Data
    public static class GitHubTokenResponse {
        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("token_type")
        private String tokenType;

        private String scope;

        private String error;

        @JsonProperty("error_description")
        private String errorDescription;
    }

    @Data
    public static class GitHubUser {
        private Long id;
        private String login;
        private String email;

        @JsonProperty("avatar_url")
        private String avatarUrl;

        private String name;

        @JsonProperty("public_repos")
        private Integer publicRepos;
    }
}
