package com.frame.api.project.repository;

import com.frame.api.project.entity.Project;
import com.frame.api.project.entity.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
