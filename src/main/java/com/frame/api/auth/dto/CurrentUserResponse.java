package com.frame.api.auth.dto;

import java.util.UUID;

public record CurrentUserResponse(
        UUID id,
        String fullName,
        String email,
        String role
) {
}
