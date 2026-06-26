package com.frame.api.reference.repository;

import com.frame.api.reference.entity.SceneReference;
import org.springframework.data.jpa.repository.JpaRepository;
import com.frame.api.reference.entity.SceneReferenceType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
            SELECT r FROM SceneReference r
            WHERE r.scene.project.workspace.owner.id = :ownerId
            AND (:type IS NULL OR r.type = :type)
            ORDER BY r.updatedAt DESC
            """)
    List<SceneReference> findByOwnerWithFilters(
            @Param("ownerId") UUID ownerId,
            @Param("type") SceneReferenceType type
    );

    @Query("""
            SELECT r FROM SceneReference r
            WHERE r.scene.id = :sceneId
            AND r.scene.project.workspace.owner.id = :ownerId
            AND (:type IS NULL OR r.type = :type)
            ORDER BY r.updatedAt DESC
            """)
    List<SceneReference> findBySceneWithFilters(
            @Param("sceneId") UUID sceneId,
            @Param("ownerId") UUID ownerId,
            @Param("type") SceneReferenceType type
    );
}
