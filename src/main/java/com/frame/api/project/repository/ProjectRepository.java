package com.frame.api.project.repository;

import com.frame.api.project.entity.Project;
import com.frame.api.project.entity.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    List<Project> findByWorkspaceId(UUID workspaceId);
    List<Project> findByWorkspace_Owner_Id(UUID ownerId);

    long countByWorkspace_Owner_Id(UUID ownerId);

    long countByWorkspace_Owner_IdAndStatus(UUID ownerId, ProjectStatus status);

    boolean existsByNameIgnoreCaseAndWorkspaceId(String name, UUID workspaceId);

    boolean existsByNameIgnoreCaseAndWorkspaceIdAndIdNot(
            String name,
            UUID workspaceId,
            UUID id
    );

    @Query("""
            SELECT p FROM Project p
            WHERE p.workspace.owner.id = :ownerId
            AND (:status IS NULL OR p.status = :status)
            ORDER BY p.updatedAt DESC
            """)
    List<Project> findByOwnerWithFilters(
            @Param("ownerId") UUID ownerId,
            @Param("status") ProjectStatus status
    );

    @Query("""
            SELECT p FROM Project p
            WHERE p.workspace.id = :workspaceId
            AND p.workspace.owner.id = :ownerId
            AND (:status IS NULL OR p.status = :status)
            ORDER BY p.updatedAt DESC
            """)
    List<Project> findByWorkspaceWithFilters(
            @Param("workspaceId") UUID workspaceId,
            @Param("ownerId") UUID ownerId,
            @Param("status") ProjectStatus status
    );
}
