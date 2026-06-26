package com.frame.api.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateSceneCommentRequest(

        @NotBlank(message = "Comment content is required")
        @Size(max = 2000, message = "Comment content must have at most 2000 characters")
        String content
) {
}
