package com.yilmaz.sprintmate.modules.lobby.entity;

import com.yilmaz.sprintmate.modules.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "matchmaking_queue")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchmakingQueue {

    @Id
    @Column(name = "user_id", columnDefinition = "UNIQUEIDENTIFIER")
    private UUID userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @Column(nullable = false, length = 20)
    private String role; // FRONTEND, BACKEND

    @Column(name = "joined_at")
    @Builder.Default
    private LocalDateTime joinedAt = LocalDateTime.now();
}
