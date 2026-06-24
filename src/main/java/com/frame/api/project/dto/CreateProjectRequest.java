package com.frame.api.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateProjectRequest(
        @NotBlank(message = "Project name is required")
        @Size(max = 140, message = "Project name must have at most 140 characters")
        String name,

        @Size(max = 700, message = "Description must have at most 700 characters")
        String description,

        @NotNull(message = "Workspace id is required")
        UUID workspaceId
) {
}
