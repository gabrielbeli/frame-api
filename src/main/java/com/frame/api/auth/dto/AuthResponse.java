package com.frame.api.auth.dto;

import java.time.Instant;
import java.util.UUID;

public record AuthResponse(
        String accessToken,
        String tokenType,
        Instant expiresAt,
        UUID userId,
        String fullName,
        String email,
        String role
) {
}
