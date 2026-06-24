package com.frame.api.workspace.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateWorkspaceRequest(
        @NotBlank(message = "Workspace name is required")
        @Size(max = 120, message = "Workspace name must have at most 120 characters")
        String name,

        @Size(max = 500, message = "Description must have at most 500 characters")
        String description
) {
}
