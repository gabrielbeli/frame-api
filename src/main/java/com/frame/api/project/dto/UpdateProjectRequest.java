package com.frame.api.project.dto;

import jakarta.validation.constraints.Size;

public record UpdateProjectRequest(

        @Size(max = 140, message = "Project name must have at most 140 characters")
        String name,

        @Size(max = 700, message = "Description must have at most 700 characters")
        String description
) {
}
