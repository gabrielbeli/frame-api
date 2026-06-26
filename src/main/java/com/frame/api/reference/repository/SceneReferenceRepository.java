package com.frame.api.reference.repository;

import com.frame.api.reference.entity.SceneReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SceneReferenceRepository extends JpaRepository<SceneReference, UUID> {

    List<SceneReference> findBySceneIdOrderByUpdatedAtDesc(UUID sceneId);

    List<SceneReference> findByScene_Project_Workspace_Owner_IdOrderByUpdatedAtDesc(UUID ownerId);

    boolean existsByTitleIgnoreCaseAndSceneId(String title, UUID sceneId);

    boolean existsByTitleIgnoreCaseAndSceneIdAndIdNot(
            String title,
            UUID sceneId,
            UUID id
    );
}
