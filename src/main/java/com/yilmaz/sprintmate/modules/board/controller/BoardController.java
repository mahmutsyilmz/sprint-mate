package com.yilmaz.sprintmate.modules.board.controller;

import com.yilmaz.sprintmate.common.dto.ApiResponse;
import com.yilmaz.sprintmate.modules.auth.entity.User;
import com.yilmaz.sprintmate.modules.board.dto.MoveTaskRequest;
import com.yilmaz.sprintmate.modules.board.dto.TeamBoardResponse;
import com.yilmaz.sprintmate.modules.board.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
@Tag(name = "Board", description = "Kanban board operations")
public class BoardController {

    private final TeamService teamService;

    @GetMapping("/my-team")
    @Operation(summary = "Get My Board", description = "Get current user's team, tasks and members")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<TeamBoardResponse>> getMyBoard(
            @AuthenticationPrincipal User user) {

        TeamBoardResponse response = teamService.getTeamBoard(user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PatchMapping("/task/{taskId}/move")
    @Operation(summary = "Move Task", description = "Update task status (e.g. TODO -> IN_PROGRESS)")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<TeamBoardResponse>> moveTask(
            @AuthenticationPrincipal User user,
            @PathVariable UUID taskId,
            @Valid @RequestBody MoveTaskRequest request) {

        TeamBoardResponse response = teamService.moveTask(taskId, request.getStatus(), user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
