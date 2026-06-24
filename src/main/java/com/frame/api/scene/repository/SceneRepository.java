package com.frame.api.scene.repository;

import com.frame.api.scene.entity.Scene;
import com.frame.api.scene.entity.SceneStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SceneRepository extends JpaRepository<Scene,UUID> {

    List<Scene> findByProjectIdOrderByPositionAsc(UUID projectId);
    List<Scene> findByProject_Workspace_Owner_IdOrderByPositionAsc(UUID ownerId);

    long countByProject_Workspace_Owner_Id(UUID ownerId);

    long countByProject_Workspace_Owner_IdAndStatus(UUID ownerId, SceneStatus status);

    List<Scene> findTop5ByProject_Workspace_Owner_IdOrderByUpdatedAtDesc(UUID ownerId);

    boolean existsByTitleIgnoreCaseAndProjectId(String title, UUID projectId);

    boolean existsByTitleIgnoreCaseAndProjectIdAndIdNot(
            String title,
            UUID projectId,
            UUID id
    );
}
