package com.frame.api.scene.dto;

import com.frame.api.scene.entity.SceneStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateSceneStatusRequest(

        @NotNull(message = "Scene status is required")
        SceneStatus status
) {
}
