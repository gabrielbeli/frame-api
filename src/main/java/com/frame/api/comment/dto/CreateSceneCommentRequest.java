package com.frame.api.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CreateSceneCommentRequest(

        @NotBlank(message = "Comment content is required")
        @Size(max = 2000, message = "Comment content must have at most 2000 characters")
        String content,

        @NotNull(message = "Scene id is required")
        UUID sceneId
) {
}
