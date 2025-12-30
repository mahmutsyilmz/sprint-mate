package com.yilmaz.sprintmate.modules.lobby.controller;

import com.yilmaz.sprintmate.common.dto.ApiResponse;
import com.yilmaz.sprintmate.modules.auth.entity.User;
import com.yilmaz.sprintmate.modules.lobby.dto.LobbyStatusResponse;
import com.yilmaz.sprintmate.modules.lobby.service.LobbyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lobby")
@RequiredArgsConstructor
@Tag(name = "Lobby", description = "Matchmaking operations")
public class LobbyController {

    private final LobbyService lobbyService;

    @PostMapping("/join")
    @Operation(summary = "Join Matchmaking Queue", description = "Enter the pool to find a team")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ApiResponse<LobbyStatusResponse>> joinLobby(
            @AuthenticationPrincipal User user) {

        LobbyStatusResponse response = lobbyService.joinLobby(user);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
