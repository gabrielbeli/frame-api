package com.frame.api.project.service;

import com.frame.api.project.dto.CreateProjectRequest;
import com.frame.api.project.dto.ProjectResponse;
import com.frame.api.project.entity.Project;
import com.frame.api.project.entity.ProjectStatus;
import com.frame.api.project.repository.ProjectRepository;
import com.frame.api.workspace.entity.Workspace;
import com.frame.api.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final WorkspaceRepository workspaceRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            WorkspaceRepository workspaceRepository
    ) {
        this.projectRepository = projectRepository;
        this.workspaceRepository = workspaceRepository;
    }

    @Transactional
    public ProjectResponse create(CreateProjectRequest request) {
        Workspace workspace = workspaceRepository.findById(request.workspaceId())
                .orElseThrow(() -> new IllegalArgumentException("Workspace not found"));

        String normalizedName = request.name().trim();

        boolean projectAlreadyExists = projectRepository
                .existsByNameIgnoreCaseAndWorkspaceId(normalizedName, workspace.getId());

        if (projectAlreadyExists) {
            throw new IllegalArgumentException("Project name is already in use for this workspace");
        }

        Project project = new Project(
                normalizedName,
                normalizeDescription(request.description()),
                ProjectStatus.DRAFT,
                workspace
        );

        Project savedProject = projectRepository.save(project);

        return ProjectResponse.fromEntity(savedProject);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> findAll() {
        return projectRepository.findAll()
                .stream()
                .map(ProjectResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> findByWorkspaceId(UUID workspaceId) {
        return projectRepository.findByWorkspaceId(workspaceId)
                .stream()
                .map(ProjectResponse::fromEntity)
                .toList();
    }

    private String normalizeDescription(String description) {
        if (description == null || description.isBlank()) {
            return null;
        }

        return description.trim();
    }
}
