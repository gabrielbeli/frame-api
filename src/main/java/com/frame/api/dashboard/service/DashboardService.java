package com.frame.api.dashboard.service;

import com.frame.api.dashboard.dto.DashboardRecentSceneResponse;
import com.frame.api.dashboard.dto.DashboardSummaryResponse;
import com.frame.api.project.entity.ProjectStatus;
import com.frame.api.project.repository.ProjectRepository;
import com.frame.api.scene.entity.SceneStatus;
import com.frame.api.scene.repository.SceneRepository;
import com.frame.api.workspace.repository.WorkspaceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DashboardService {

    private final WorkspaceRepository workspaceRepository;
    private final ProjectRepository projectRepository;
    private final SceneRepository sceneRepository;

    public DashboardService(
            WorkspaceRepository workspaceRepository,
            ProjectRepository projectRepository,
            SceneRepository sceneRepository
    ) {
        this.workspaceRepository = workspaceRepository;
        this.projectRepository = projectRepository;
        this.sceneRepository = sceneRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getSummary(UUID ownerId) {
        long totalWorkspaces = workspaceRepository.countByOwnerId(ownerId);

        long totalProjects = projectRepository.countByWorkspace_Owner_Id(ownerId);
        long activeProjects = projectRepository.countByWorkspace_Owner_IdAndStatus(
                ownerId,
                ProjectStatus.ACTIVE
        );
        long archivedProjects = projectRepository.countByWorkspace_Owner_IdAndStatus(
                ownerId,
                ProjectStatus.ARCHIVED
        );

        long totalScenes = sceneRepository.countByProject_Workspace_Owner_Id(ownerId);
        long scenesInProgress = sceneRepository.countByProject_Workspace_Owner_IdAndStatus(
                ownerId,
                SceneStatus.IN_PROGRESS
        );
        long scenesInReview = sceneRepository.countByProject_Workspace_Owner_IdAndStatus(
                ownerId,
                SceneStatus.REVIEW
        );
        long approvedScenes = sceneRepository.countByProject_Workspace_Owner_IdAndStatus(
                ownerId,
                SceneStatus.APPROVED
        );

        var recentScenes = sceneRepository
                .findTop5ByProject_Workspace_Owner_IdOrderByUpdatedAtDesc(ownerId)
                .stream()
                .map(DashboardRecentSceneResponse::fromEntity)
                .toList();

        return new DashboardSummaryResponse(
                totalWorkspaces,
                totalProjects,
                activeProjects,
                archivedProjects,
                totalScenes,
                scenesInProgress,
                scenesInReview,
                approvedScenes,
                recentScenes
        );
    }
}
