package com.frame.api.project.service;

import com.frame.api.common.exception.ForbiddenException;
import com.frame.api.project.dto.CreateProjectRequest;
import com.frame.api.project.dto.ProjectResponse;
import com.frame.api.project.dto.UpdateProjectRequest;
import com.frame.api.project.dto.UpdateProjectStatusRequest;
import com.frame.api.project.entity.Project;
import com.frame.api.project.entity.ProjectStatus;
import com.frame.api.project.repository.ProjectRepository;
import com.frame.api.workspace.entity.Workspace;
import com.frame.api.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.frame.api.common.exception.ConflictException;
import com.frame.api.common.exception.ResourceNotFoundException;

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
    public ProjectResponse create(CreateProjectRequest request, UUID ownerId) {
        Workspace workspace = workspaceRepository.findById(request.workspaceId())
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));

        ensureWorkspaceBelongsToUser(workspace, ownerId);

        String normalizedName = request.name().trim();

        boolean projectAlreadyExists = projectRepository
                .existsByNameIgnoreCaseAndWorkspaceId(normalizedName, workspace.getId());

        if (projectAlreadyExists) {
            throw new ConflictException("Project name is already in use for this workspace");
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
    public List<ProjectResponse> findAllByOwnerId(UUID ownerId) {
        return projectRepository.findByWorkspace_Owner_Id(ownerId)
                .stream()
                .map(ProjectResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse findById(UUID projectId, UUID ownerId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        ensureWorkspaceBelongsToUser(project.getWorkspace(), ownerId);

        return ProjectResponse.fromEntity(project);
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> findByWorkspaceId(UUID workspaceId, UUID ownerId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));

        ensureWorkspaceBelongsToUser(workspace, ownerId);

        return projectRepository.findByWorkspaceId(workspaceId)
                .stream()
                .map(ProjectResponse::fromEntity)
                .toList();
    }

    @Transactional
    public ProjectResponse update(
            UUID projectId,
            UpdateProjectRequest request,
            UUID ownerId
    ) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        ensureWorkspaceBelongsToUser(project.getWorkspace(), ownerId);

        if (request.name() != null) {
            updateName(project, request.name());
        }

        if (request.description() != null) {
            project.setDescription(normalizeDescription(request.description()));
        }

        Project updatedProject = projectRepository.save(project);

        return ProjectResponse.fromEntity(updatedProject);
    }

    @Transactional
    public ProjectResponse updateStatus(
            UUID projectId,
            UpdateProjectStatusRequest request,
            UUID ownerId
    ) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        ensureWorkspaceBelongsToUser(project.getWorkspace(), ownerId);

        project.setStatus(request.status());

        Project updatedProject = projectRepository.save(project);

        return ProjectResponse.fromEntity(updatedProject);
    }

    private void updateName(Project project, String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Project name cannot be blank");
        }

        String normalizedName = name.trim();

        boolean nameAlreadyExists = projectRepository
                .existsByNameIgnoreCaseAndWorkspaceIdAndIdNot(
                        normalizedName,
                        project.getWorkspace().getId(),
                        project.getId()
                );

        if (nameAlreadyExists) {
            throw new ConflictException("Project name is already in use for this workspace");
        }

        project.setName(normalizedName);
    }

    private void ensureWorkspaceBelongsToUser(Workspace workspace, UUID ownerId) {
        if (!workspace.getOwner().getId().equals(ownerId)) {
            throw new ForbiddenException("You do not have permission to access this workspace");
        }
    }

    private String normalizeDescription(String description) {
        if (description == null || description.isBlank()) {
            return null;
        }

        return description.trim();
    }
}
