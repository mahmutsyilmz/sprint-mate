package com.yilmaz.sprintmate.modules.ai.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "system_prompts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemPrompt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "category_name", nullable = false, length = 50)
    private String categoryName; // FinTech, Health, E-Commerce

    @Column(name = "prompt_template", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String promptTemplate;

    @Column(name = "required_role_distribution", length = 50)
    @Builder.Default
    private String requiredRoleDistribution = "2FE_1BE";

    @Column(name = "difficulty_level")
    @Builder.Default
    private Byte difficultyLevel = 7;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
