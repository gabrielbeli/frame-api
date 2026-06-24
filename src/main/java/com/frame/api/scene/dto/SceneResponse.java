package com.frame.api.scene.dto;

import com.frame.api.scene.entity.Scene;
import com.frame.api.scene.entity.SceneStatus;

import java.time.Instant;
import java.util.UUID;

public record SceneResponse(
        UUID id,
        String title,
        String summary,
        Integer position,
        String layer,
        SceneStatus status,
        UUID projectId,
        String projectName,
        Instant createdAt,
        Instant updatedAt
) {
    public static SceneResponse fromEntity(Scene scene) {
        return new SceneResponse(
                scene.getId(),
                scene.getTitle(),
                scene.getSummary(),
                scene.getPosition(),
                scene.getLayer(),
                scene.getStatus(),
                scene.getProject().getId(),
                scene.getProject().getName(),
                scene.getCreatedAt(),
                scene.getUpdatedAt()
        );
    }
}
