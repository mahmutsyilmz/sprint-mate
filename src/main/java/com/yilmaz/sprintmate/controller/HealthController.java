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
 * API'nin çalışıp çalışmadığını kontrol etmek için.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Health", description = "API sağlık kontrolü endpoint'leri")
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "Health Check", description = "API'nin çalışıp çalışmadığını kontrol eder")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "API çalışıyor"),
            @ApiResponse(responseCode = "500", description = "API çalışmıyor")
    })
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "application", "Sprint Mate",
                "timestamp", LocalDateTime.now().toString(),
                "version", "1.0.0"));
    }
}
