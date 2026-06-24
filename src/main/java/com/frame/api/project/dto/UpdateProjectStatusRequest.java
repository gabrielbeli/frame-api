package com.frame.api.project.dto;

import com.frame.api.project.entity.ProjectStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateProjectStatusRequest(

        @NotNull(message = "Project status is required")
        ProjectStatus status
) {
}
