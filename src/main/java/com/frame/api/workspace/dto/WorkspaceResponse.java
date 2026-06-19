package com.frame.api.workspace.dto;

import com.frame.api.workspace.entity.Workspace;

import java.time.Instant;
import java.util.UUID;

public record WorkspaceResponse(
        UUID id,
        String name,
        String description,
        UUID ownerId,
        String ownerName,
        Instant createdAt,
        Instant updatedAt
) {
    public static WorkspaceResponse fromEntity(Workspace workspace) {
        return new WorkspaceResponse(
                workspace.getId(),
                workspace.getName(),
                workspace.getDescription(),
                workspace.getOwner().getId(),
                workspace.getOwner().getFullName(),
                workspace.getCreatedAt(),
                workspace.getUpdatedAt()
        );
    }
}
