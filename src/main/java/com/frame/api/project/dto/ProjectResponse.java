package com.frame.api.project.dto;

import com.frame.api.project.entity.Project;
import com.frame.api.project.entity.ProjectStatus;

import java.time.Instant;
import java.util.UUID;

public record ProjectResponse(
        UUID id,
        String name,
        String description,
        ProjectStatus status,
        UUID workspaceId,
        String workspaceName,
        Instant createdAt,
        Instant updatedAt
) {
    public static ProjectResponse fromEntity(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getStatus(),
                project.getWorkspace().getId(),
                project.getWorkspace().getName(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}
