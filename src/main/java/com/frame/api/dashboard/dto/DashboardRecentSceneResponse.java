package com.frame.api.dashboard.dto;

import com.frame.api.scene.entity.Scene;
import com.frame.api.scene.entity.SceneStatus;

import java.time.Instant;
import java.util.UUID;

public record DashboardRecentSceneResponse(
        UUID id,
        String title,
        String projectName,
        SceneStatus status,
        Integer position,
        String layer,
        Instant updatedAt
) {
    public static DashboardRecentSceneResponse fromEntity(Scene scene) {
        return new DashboardRecentSceneResponse(
                scene.getId(),
                scene.getTitle(),
                scene.getProject().getName(),
                scene.getStatus(),
                scene.getPosition(),
                scene.getLayer(),
                scene.getUpdatedAt()
        );
    }
}
