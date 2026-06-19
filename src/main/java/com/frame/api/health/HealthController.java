package com.frame.api.health;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
public class HealthController {
    @GetMapping("/api/health")
    public ResponseEntity<HealthResponse> healthCheck() {
        HealthResponse response = new HealthResponse(
                "Frame API is running",
                "UP",
                Instant.now().toString()
        );

        return ResponseEntity.ok(response);
    }

    public record HealthResponse(
            String message,
            String status,
            String timestamp
    ) {
    }
}
