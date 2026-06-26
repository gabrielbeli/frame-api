package com.frame.api.workspace.service;

import com.frame.api.user.entity.FrameUser;
import com.frame.api.user.repository.UserRepository;
import com.frame.api.workspace.dto.CreateWorkspaceRequest;
import com.frame.api.workspace.dto.UpdateWorkspaceRequest;
import com.frame.api.workspace.dto.WorkspaceResponse;
import com.frame.api.workspace.entity.Workspace;
import com.frame.api.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.frame.api.common.exception.ConflictException;
import com.frame.api.common.exception.ResourceNotFoundException;
import com.frame.api.common.exception.ForbiddenException;
import com.frame.api.activity.entity.ActivityResourceType;
import com.frame.api.activity.entity.ActivityType;
import com.frame.api.activity.service.ActivityService;

import java.util.List;
import java.util.UUID;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final ActivityService activityService;

    public WorkspaceService(
            WorkspaceRepository workspaceRepository,
            UserRepository userRepository,
            ActivityService activityService
    ) {
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
        this.activityService = activityService;
    }

    @Transactional
    public WorkspaceResponse create(CreateWorkspaceRequest request, UUID ownerId) {
        FrameUser owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

        String normalizedName = request.name().trim();

        boolean workspaceAlreadyExists = workspaceRepository
                .existsByNameIgnoreCaseAndOwnerId(normalizedName, owner.getId());

        if (workspaceAlreadyExists) {
            throw new ConflictException("Workspace name is already in use for this owner");
        }

        Workspace workspace = new Workspace(
                normalizedName,
                normalizeDescription(request.description()),
                owner
        );

        Workspace savedWorkspace = workspaceRepository.save(workspace);

        activityService.log(
                ownerId,
                ActivityType.WORKSPACE_CREATED,
                ActivityResourceType.WORKSPACE,
                savedWorkspace.getId(),
                savedWorkspace.getName(),
                "Created workspace \"" + savedWorkspace.getName() + "\""
        );

        return WorkspaceResponse.fromEntity(savedWorkspace);
    }

    @Transactional(readOnly = true)
    public List<WorkspaceResponse> findByOwnerId(UUID ownerId) {
        return workspaceRepository.findByOwnerId(ownerId)
                .stream()
                .map(WorkspaceResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public WorkspaceResponse findById(UUID workspaceId, UUID ownerId) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));

        ensureWorkspaceBelongsToUser(workspace, ownerId);

        return WorkspaceResponse.fromEntity(workspace);
    }

    @Transactional
    public WorkspaceResponse update(
            UUID workspaceId,
            UpdateWorkspaceRequest request,
            UUID ownerId
    ) {
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));

        ensureWorkspaceBelongsToUser(workspace, ownerId);

        if (request.name() != null) {
            updateName(workspace, request.name(), ownerId);
        }

        if (request.description() != null) {
            workspace.setDescription(normalizeDescription(request.description()));
        }

        Workspace updatedWorkspace = workspaceRepository.save(workspace);

        activityService.log(
                ownerId,
                ActivityType.WORKSPACE_UPDATED,
                ActivityResourceType.WORKSPACE,
                updatedWorkspace.getId(),
                updatedWorkspace.getName(),
                "Updated workspace \"" + updatedWorkspace.getName() + "\""
        );

        return WorkspaceResponse.fromEntity(updatedWorkspace);
    }

    private void updateName(Workspace workspace, String name, UUID ownerId) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("Workspace name cannot be blank");
        }

        String normalizedName = name.trim();

        boolean nameAlreadyExists = workspaceRepository
                .existsByNameIgnoreCaseAndOwnerIdAndIdNot(
                        normalizedName,
                        ownerId,
                        workspace.getId()
                );

        if (nameAlreadyExists) {
            throw new ConflictException("Workspace name is already in use for this owner");
        }

        workspace.setName(normalizedName);
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
