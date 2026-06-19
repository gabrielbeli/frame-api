package com.frame.api.project.repository;

import com.frame.api.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    List<Project> findByWorkspaceId(UUID workspaceId);

    boolean existsByNameIgnoreCaseAndWorkspaceId(String name, UUID workspaceId);
}
