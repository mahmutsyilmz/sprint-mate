package com.yilmaz.sprintmate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Health Check Controller
 * 
 * To check if the API is running.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Health", description = "API health check endpoints")
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "Check if the API is running")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API is running"),
            @ApiResponse(responseCode = "500", description = "API is not running")
    })
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "application", "Sprint Mate",
                "timestamp", LocalDateTime.now().toString(),
                "version", "1.0.0"));
    }
}
