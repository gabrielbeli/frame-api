package com.frame.api.workspace.service;

import com.frame.api.user.entity.FrameUser;
import com.frame.api.user.repository.UserRepository;
import com.frame.api.workspace.dto.CreateWorkspaceRequest;
import com.frame.api.workspace.dto.WorkspaceResponse;
import com.frame.api.workspace.entity.Workspace;
import com.frame.api.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.frame.api.common.exception.ConflictException;
import com.frame.api.common.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;

    public WorkspaceService(
            WorkspaceRepository workspaceRepository,
            UserRepository userRepository
    ) {
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
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

        return WorkspaceResponse.fromEntity(savedWorkspace);
    }

    @Transactional(readOnly = true)
    public List<WorkspaceResponse> findAll() {
        return workspaceRepository.findAll()
                .stream()
                .map(WorkspaceResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<WorkspaceResponse> findByOwnerId(java.util.UUID ownerId) {
        return workspaceRepository.findByOwnerId(ownerId)
                .stream()
                .map(WorkspaceResponse::fromEntity)
                .toList();
    }

    private String normalizeDescription(String description) {
        if (description == null || description.isBlank()) {
            return null;
        }

        return description.trim();
    }
}
