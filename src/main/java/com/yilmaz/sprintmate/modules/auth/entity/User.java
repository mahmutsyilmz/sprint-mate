package com.yilmaz.sprintmate.modules.auth.entity;

import com.yilmaz.sprintmate.modules.board.entity.Team;
import jakarta.persistence.*;
import com.yilmaz.sprintmate.modules.auth.enums.UserRole;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @UuidGenerator
    @Column(columnDefinition = "UNIQUEIDENTIFIER")
    private UUID id;

    @Column(name = "github_id", nullable = false, unique = true, length = 50)
    private String githubId;

    @Column(nullable = false, length = 100)
    private String username;

    @Column(length = 255)
    private String email;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 20)
    private UserRole role; // FRONTEND, BACKEND, null (if not selected)

    @Column(name = "xp_points")
    @Builder.Default
    private Integer xpPoints = 0;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    // Relationship: One Team has Many Users
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    // UserDetails Implementation

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (role == null) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return null; // No password for OAuth users
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }
}
