package com.yilmaz.sprintmate.modules.board.dto;

import com.yilmaz.sprintmate.modules.board.enums.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MoveTaskRequest {
    @NotNull(message = "Status is required")
    private TaskStatus status;

    // Optional: PR Link for REVIEW status
    private String prLink;
}
