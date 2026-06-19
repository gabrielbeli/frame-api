package com.frame.api.user.dto;

import com.frame.api.user.entity.FrameUser;
import com.frame.api.user.entity.UserRole;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String fullName,
        String email,
        UserRole role,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserResponse fromEntity(FrameUser user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
