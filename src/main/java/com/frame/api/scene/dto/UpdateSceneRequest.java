package com.frame.api.scene.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public record UpdateSceneRequest(

        @Size(max = 160, message = "Scene title must have at most 160 characters")
        String title,

        @Size(max = 1000, message = "Summary must have at most 1000 characters")
        String summary,

        @Min(value = 0, message = "Position must be zero or greater")
        Integer position,

        @Size(max = 80, message = "Layer must have at most 80 characters")
        String layer
) {
}
