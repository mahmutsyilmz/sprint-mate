package com.yilmaz.sprintmate.modules.board.entity;

import com.yilmaz.sprintmate.modules.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @UuidGenerator
    @Column(columnDefinition = "UNIQUEIDENTIFIER")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_user_id")
    private User assignedUser;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "task_type", length = 20)
    @Builder.Default
    private String taskType = "FEATURE"; // BUG, FEATURE, CHORE

    @Column(length = 20)
    @Builder.Default
    private String status = "TODO"; // TODO, IN_PROGRESS, REVIEW, DONE

    @Column(name = "column_order")
    @Builder.Default
    private Integer columnOrder = 0;

    @Column(name = "pr_link", length = 500)
    private String prLink;

    @Column(name = "difficulty_score")
    private Byte difficultyScore;

    @Version // Optimistic Locking
    @Builder.Default
    private Integer version = 0;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
