package com.frame.api.comment.service;

import com.frame.api.activity.entity.ActivityResourceType;
import com.frame.api.activity.entity.ActivityType;
import com.frame.api.activity.service.ActivityService;
import com.frame.api.comment.dto.CreateSceneCommentRequest;
import com.frame.api.comment.dto.SceneCommentResponse;
import com.frame.api.comment.dto.UpdateSceneCommentRequest;
import com.frame.api.comment.entity.SceneComment;
import com.frame.api.comment.repository.SceneCommentRepository;
import com.frame.api.common.exception.ForbiddenException;
import com.frame.api.common.exception.ResourceNotFoundException;
import com.frame.api.scene.entity.Scene;
import com.frame.api.scene.repository.SceneRepository;
import com.frame.api.user.entity.FrameUser;
import com.frame.api.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class SceneCommentService {

    private final SceneCommentRepository commentRepository;
    private final SceneRepository sceneRepository;
    private final UserRepository userRepository;
    private final ActivityService activityService;

    public SceneCommentService(
            SceneCommentRepository commentRepository,
            SceneRepository sceneRepository,
            UserRepository userRepository,
            ActivityService activityService
    ) {
        this.commentRepository = commentRepository;
        this.sceneRepository = sceneRepository;
        this.userRepository = userRepository;
        this.activityService = activityService;
    }

    @Transactional
    public SceneCommentResponse create(CreateSceneCommentRequest request, UUID authorId) {
        Scene scene = sceneRepository.findById(request.sceneId())
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        ensureSceneBelongsToUser(scene, authorId);

        FrameUser author = userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        SceneComment comment = new SceneComment(
                normalizeContent(request.content()),
                scene,
                author
        );

        SceneComment savedComment = commentRepository.save(comment);

        activityService.log(
                authorId,
                ActivityType.COMMENT_CREATED,
                ActivityResourceType.COMMENT,
                savedComment.getId(),
                scene.getTitle(),
                "Commented on scene \"" + scene.getTitle() + "\""
        );

        return SceneCommentResponse.fromEntity(savedComment);
    }

    @Transactional(readOnly = true)
    public List<SceneCommentResponse> findAllByOwnerId(UUID ownerId) {
        return commentRepository.findByScene_Project_Workspace_Owner_IdOrderByCreatedAtDesc(ownerId)
                .stream()
                .map(SceneCommentResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public SceneCommentResponse findById(UUID commentId, UUID userId) {
        SceneComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        ensureSceneBelongsToUser(comment.getScene(), userId);

        return SceneCommentResponse.fromEntity(comment);
    }

    @Transactional(readOnly = true)
    public List<SceneCommentResponse> findBySceneId(UUID sceneId, UUID userId) {
        Scene scene = sceneRepository.findById(sceneId)
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        ensureSceneBelongsToUser(scene, userId);

        return commentRepository.findBySceneIdOrderByCreatedAtAsc(sceneId)
                .stream()
                .map(SceneCommentResponse::fromEntity)
                .toList();
    }

    @Transactional
    public SceneCommentResponse update(
            UUID commentId,
            UpdateSceneCommentRequest request,
            UUID userId
    ) {
        SceneComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        ensureSceneBelongsToUser(comment.getScene(), userId);
        ensureCommentBelongsToUser(comment, userId);

        comment.setContent(normalizeContent(request.content()));

        SceneComment updatedComment = commentRepository.save(comment);

        activityService.log(
                userId,
                ActivityType.COMMENT_UPDATED,
                ActivityResourceType.COMMENT,
                updatedComment.getId(),
                updatedComment.getScene().getTitle(),
                "Updated comment on scene \"" + updatedComment.getScene().getTitle() + "\""
        );

        return SceneCommentResponse.fromEntity(updatedComment);
    }

    @Transactional
    public void delete(UUID commentId, UUID userId) {
        SceneComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));

        ensureSceneBelongsToUser(comment.getScene(), userId);
        ensureCommentBelongsToUser(comment, userId);

        String sceneTitle = comment.getScene().getTitle();

        activityService.log(
                userId,
                ActivityType.COMMENT_DELETED,
                ActivityResourceType.COMMENT,
                comment.getId(),
                sceneTitle,
                "Deleted comment on scene \"" + sceneTitle + "\""
        );

        commentRepository.delete(comment);
    }

    private void ensureSceneBelongsToUser(Scene scene, UUID userId) {
        UUID ownerId = scene.getProject().getWorkspace().getOwner().getId();

        if (!ownerId.equals(userId)) {
            throw new ForbiddenException("You do not have permission to access this scene");
        }
    }

    private void ensureCommentBelongsToUser(SceneComment comment, UUID userId) {
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException("You do not have permission to modify this comment");
        }
    }

    private String normalizeContent(String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Comment content cannot be blank");
        }

        return content.trim();
    }
}
