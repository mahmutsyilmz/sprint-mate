package com.yilmaz.sprintmate.modules.board.dto;

import com.yilmaz.sprintmate.modules.auth.dto.UserDto;
import com.yilmaz.sprintmate.modules.board.enums.TaskStatus;
import com.yilmaz.sprintmate.modules.board.enums.TaskType;
import com.yilmaz.sprintmate.modules.board.enums.TeamStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class TeamBoardResponse {
    private UUID teamId;
    private String teamName;
    private TeamStatus teamStatus;
    private String projectStory;
    private LocalDateTime sprintEndDate;

    private List<UserDto> members;
    private List<TaskDto> tasks;

    @Data
    @Builder
    public static class TaskDto {
        private UUID id;
        private String title;
        private String description;
        private TaskStatus status;
        private TaskType type;
        private Byte difficultyScore;
        private UUID assignedUserId; // Can be null
        private String assignedUserAvatar; // Helper for UI
    }
}
