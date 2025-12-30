package com.yilmaz.sprintmate.modules.lobby.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LobbyStatusResponse {
    private boolean inQueue;
    private boolean matched;
    private String message;
    private String teamId; // Null if not matched yet
    private Integer queuePosition; // Optional: show user their place in line
}
