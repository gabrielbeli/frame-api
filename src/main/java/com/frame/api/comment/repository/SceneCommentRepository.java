package com.frame.api.comment.repository;

import com.frame.api.comment.entity.SceneComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SceneCommentRepository extends JpaRepository<SceneComment, UUID> {

    List<SceneComment> findBySceneIdOrderByCreatedAtAsc(UUID sceneId);

    List<SceneComment> findByScene_Project_Workspace_Owner_IdOrderByCreatedAtDesc(UUID ownerId);
}
