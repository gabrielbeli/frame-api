package com.frame.api.scene.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateSceneRequest(
        @NotBlank(message = "Scene title is required")
        @Size(max = 160, message = "Scene title must have at most 160 characters")
        String title,

        @Size(max = 1000, message = "Summary must have at most 1000 characters")
        String summary,

        @Min(value = 0, message = "Position must be zero or greater")
        Integer position,

        @Size(max = 80, message = "Layer must have at most 80 characters")
        String layer,

        @NotNull(message = "Project id is required")
        UUID projectId
) {
}
