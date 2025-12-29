package com.yilmaz.sprintmate.modules.board.entity;

import com.yilmaz.sprintmate.modules.ai.entity.SystemPrompt;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {

    @Id
    @UuidGenerator
    @Column(columnDefinition = "UNIQUEIDENTIFIER")
    private UUID id;

    @Column(length = 100)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prompt_id")
    private SystemPrompt prompt;

    @Column(nullable = false, length = 20)
    private String status; // LOBBY, GENERATING, IN_SPRINT, COMPLETED, ABANDONED

    @Column(name = "ai_project_story", columnDefinition = "NVARCHAR(MAX)")
    private String aiProjectStory; // JSON format

    @Column(name = "repo_url", length = 500)
    private String repoUrl;

    @Column(name = "communication_url", length = 500)
    private String communicationUrl;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
