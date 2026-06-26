package com.frame.api.reference.dto;

import com.frame.api.reference.entity.SceneReferenceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateSceneReferenceRequest(

        @NotBlank(message = "Reference title is required")
        @Size(max = 160, message = "Reference title must have at most 160 characters")
        String title,

        @Size(max = 1000, message = "Description must have at most 1000 characters")
        String description,

        @Size(max = 1000, message = "URL must have at most 1000 characters")
        String url,

        @NotNull(message = "Reference type is required")
        SceneReferenceType type,

        @NotNull(message = "Scene id is required")
        UUID sceneId
) {
}
