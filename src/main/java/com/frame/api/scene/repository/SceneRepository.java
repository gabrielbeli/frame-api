package com.frame.api.scene.repository;

import com.frame.api.scene.entity.Scene;
import com.frame.api.scene.entity.SceneStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
        SELECT s FROM Scene s
        WHERE s.project.workspace.owner.id = :ownerId
        AND (:status IS NULL OR s.status = :status)
        AND (:layer IS NULL OR LOWER(s.layer) = :layer)
        ORDER BY s.position ASC, s.updatedAt DESC
        """)
    List<Scene> findByOwnerWithFilters(
            @Param("ownerId") UUID ownerId,
            @Param("status") SceneStatus status,
            @Param("layer") String layer
    );

    @Query("""
        SELECT s FROM Scene s
        WHERE s.project.id = :projectId
        AND s.project.workspace.owner.id = :ownerId
        AND (:status IS NULL OR s.status = :status)
        AND (:layer IS NULL OR LOWER(s.layer) = :layer)
        ORDER BY s.position ASC, s.updatedAt DESC
        """)
    List<Scene> findByProjectWithFilters(
            @Param("projectId") UUID projectId,
            @Param("ownerId") UUID ownerId,
            @Param("status") SceneStatus status,
            @Param("layer") String layer
    );
}
