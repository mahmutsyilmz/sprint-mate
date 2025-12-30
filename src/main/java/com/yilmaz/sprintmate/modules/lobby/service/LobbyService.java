package com.yilmaz.sprintmate.modules.lobby.service;

import com.yilmaz.sprintmate.modules.auth.entity.User;
import com.yilmaz.sprintmate.modules.auth.enums.UserRole;

import com.yilmaz.sprintmate.modules.board.service.TeamService;
import com.yilmaz.sprintmate.modules.lobby.dto.LobbyStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Lobby Service
 * 
 * Manages the matchmaking queue using FIFO logic.
 * 
 * Rules:
 * - 1 Backend + 2 Frontends = 1 Team.
 * - Uses in-memory queue for MVP (Scale to Redis for Production).
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LobbyService {

    private final TeamService teamService;

    // Concurrent Queues for thread safety
    private final Queue<User> frontendQueue = new ConcurrentLinkedQueue<>();
    private final Queue<User> backendQueue = new ConcurrentLinkedQueue<>();

    /**
     * Join the lobby
     */
    public synchronized LobbyStatusResponse joinLobby(User user) {
        log.info("User joining lobby: {} ({})", user.getUsername(), user.getRole());

        if (isUserInQueue(user)) {
            return LobbyStatusResponse.builder()
                    .inQueue(true)
                    .message("Already in queue")
                    .build();
        }

        // Add to appropriate queue
        if (user.getRole() == UserRole.FRONTEND) {
            frontendQueue.add(user);
        } else if (user.getRole() == UserRole.BACKEND) {
            backendQueue.add(user);
        } else {
            return LobbyStatusResponse.builder()
                    .inQueue(false)
                    .message("Invalid role for matchmaking")
                    .build();
        }

        // Check if we can form a team
        tryMatchmake();

        return LobbyStatusResponse.builder()
                .inQueue(true)
                .message("Joined queue successfully")
                .queuePosition(getQueuePosition(user))
                .build();
    }

    /**
     * Check matchmaking condition
     * 1 Backend + 1 Frontend (Adjusted for easier testing)
     */
    private void tryMatchmake() {
        if (backendQueue.size() >= 1 && frontendQueue.size() >= 1) {
            log.info("Match found! Creating team...");

            User backend = backendQueue.poll();
            User frontend = frontendQueue.poll();

            List<User> teamMembers = new ArrayList<>();
            teamMembers.add(backend);
            teamMembers.add(frontend);

            teamService.createTeam(teamMembers);
        }
    }

    private boolean isUserInQueue(User user) {
        return frontendQueue.contains(user) || backendQueue.contains(user);
    }

    private int getQueuePosition(User user) {
        // Simple position check (O(n)) - okay for MVP
        if (user.getRole() == UserRole.FRONTEND) {
            return new ArrayList<>(frontendQueue).indexOf(user) + 1;
        } else {
            return new ArrayList<>(backendQueue).indexOf(user) + 1;
        }
    }
}
