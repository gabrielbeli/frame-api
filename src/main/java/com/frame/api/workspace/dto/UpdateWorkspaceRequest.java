package com.frame.api.workspace.dto;

import jakarta.validation.constraints.Size;

public record UpdateWorkspaceRequest(

        @Size(max = 120, message = "Workspace name must have at most 120 characters")
        String name,

        @Size(max = 500, message = "Description must have at most 500 characters")
        String description
) {
}
