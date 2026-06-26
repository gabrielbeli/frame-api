package com.frame.api.comment.dto;

import com.frame.api.comment.entity.SceneComment;

import java.time.Instant;
import java.util.UUID;

public record SceneCommentResponse(
        UUID id,
        String content,
        UUID sceneId,
        String sceneTitle,
        UUID authorId,
        String authorName,
        Instant createdAt,
        Instant updatedAt
) {
    public static SceneCommentResponse fromEntity(SceneComment comment) {
        return new SceneCommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getScene().getId(),
                comment.getScene().getTitle(),
                comment.getAuthor().getId(),
                comment.getAuthor().getFullName(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
