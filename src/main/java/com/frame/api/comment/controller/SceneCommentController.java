package com.frame.api.comment.controller;

import com.frame.api.comment.dto.CreateSceneCommentRequest;
import com.frame.api.comment.dto.SceneCommentResponse;
import com.frame.api.comment.dto.UpdateSceneCommentRequest;
import com.frame.api.comment.service.SceneCommentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
public class SceneCommentController {

    private final SceneCommentService commentService;

    public SceneCommentController(SceneCommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SceneCommentResponse create(
            @RequestBody @Valid CreateSceneCommentRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());

        return commentService.create(request, userId);
    }

    @GetMapping
    public List<SceneCommentResponse> findAll(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());

        return commentService.findAllByOwnerId(userId);
    }

    @GetMapping("/{commentId}")
    public SceneCommentResponse findById(
            @PathVariable UUID commentId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());

        return commentService.findById(commentId, userId);
    }

    @GetMapping("/scene/{sceneId}")
    public List<SceneCommentResponse> findBySceneId(
            @PathVariable UUID sceneId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());

        return commentService.findBySceneId(sceneId, userId);
    }

    @PatchMapping("/{commentId}")
    public SceneCommentResponse update(
            @PathVariable UUID commentId,
            @RequestBody @Valid UpdateSceneCommentRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());

        return commentService.update(commentId, request, userId);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID commentId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID userId = UUID.fromString(jwt.getSubject());

        commentService.delete(commentId, userId);
    }
}
