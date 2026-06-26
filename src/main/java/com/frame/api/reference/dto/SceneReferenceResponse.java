package com.frame.api.reference.dto;

import com.frame.api.reference.entity.SceneReference;
import com.frame.api.reference.entity.SceneReferenceType;

import java.time.Instant;
import java.util.UUID;

public record SceneReferenceResponse(
        UUID id,
        String title,
        String description,
        String url,
        SceneReferenceType type,
        UUID sceneId,
        String sceneTitle,
        UUID projectId,
        String projectName,
        Instant createdAt,
        Instant updatedAt
) {
    public static SceneReferenceResponse fromEntity(SceneReference reference) {
        return new SceneReferenceResponse(
                reference.getId(),
                reference.getTitle(),
                reference.getDescription(),
                reference.getUrl(),
                reference.getType(),
                reference.getScene().getId(),
                reference.getScene().getTitle(),
                reference.getScene().getProject().getId(),
                reference.getScene().getProject().getName(),
                reference.getCreatedAt(),
                reference.getUpdatedAt()
        );
    }
}
