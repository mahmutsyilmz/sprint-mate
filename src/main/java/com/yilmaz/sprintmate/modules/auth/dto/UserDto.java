package com.yilmaz.sprintmate.modules.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.yilmaz.sprintmate.modules.auth.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User DTO
 * 
 * Use DTO instead of returning entity directly
 * - Security: Sensitive data is hidden
 * - Flexibility: API response is independent of entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private String id;
    private String username;
    private String email;
    private String avatarUrl;
    private String role;
    private Integer xpPoints;
    private String githubId;

    /**
     * Convert entity to DTO
     */
    public static UserDto fromEntity(User user) {
        if (user == null)
            return null;

        return UserDto.builder()
                .id(user.getId().toString())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .role(user.getRole())
                .xpPoints(user.getXpPoints())
                .githubId(user.getGithubId())
                .build();
    }
}
