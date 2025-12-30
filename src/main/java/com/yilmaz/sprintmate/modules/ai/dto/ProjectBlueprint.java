package com.yilmaz.sprintmate.modules.ai.dto;

import com.yilmaz.sprintmate.modules.board.enums.TaskType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProjectBlueprint {
    private String name;
    private String description; // The "Story"
    private String category; // FinTech, E-Commerce, etc.
    private List<TaskBlueprint> tasks;

    @Data
    @Builder
    public static class TaskBlueprint {
        private String title;
        private String description;
        private TaskType type;
        private Integer difficulty; // 1-10
    }
}
