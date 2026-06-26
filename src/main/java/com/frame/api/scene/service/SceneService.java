package com.frame.api.scene.service;

import com.frame.api.common.exception.ForbiddenException;
import com.frame.api.project.entity.Project;
import com.frame.api.project.repository.ProjectRepository;
import com.frame.api.scene.dto.CreateSceneRequest;
import com.frame.api.scene.dto.SceneResponse;
import com.frame.api.scene.dto.UpdateSceneRequest;
import com.frame.api.scene.dto.UpdateSceneStatusRequest;
import com.frame.api.scene.entity.Scene;
import com.frame.api.scene.entity.SceneStatus;
import com.frame.api.scene.repository.SceneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.frame.api.common.exception.ConflictException;
import com.frame.api.common.exception.ResourceNotFoundException;
import com.frame.api.activity.entity.ActivityResourceType;
import com.frame.api.activity.entity.ActivityType;
import com.frame.api.activity.service.ActivityService;

import java.util.List;
import java.util.UUID;
import java.util.Locale;

@Service
public class SceneService {

    private final SceneRepository sceneRepository;
    private final ProjectRepository projectRepository;
    private final ActivityService activityService;

    public SceneService(
            SceneRepository sceneRepository,
            ProjectRepository projectRepository,
            ActivityService activityService
    ) {
        this.sceneRepository = sceneRepository;
        this.projectRepository = projectRepository;
        this.activityService = activityService;
    }

    @Transactional
    public SceneResponse create(CreateSceneRequest request, UUID ownerId) {
        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        ensureProjectBelongsToUser(project, ownerId);

        String normalizedTitle = request.title().trim();

        boolean sceneAlreadyExists = sceneRepository
                .existsByTitleIgnoreCaseAndProjectId(normalizedTitle, project.getId());

        if (sceneAlreadyExists) {
            throw new ConflictException("Scene title is already in use for this project");
        }

        Scene scene = new Scene(
                normalizedTitle,
                normalizeText(request.summary()),
                normalizePosition(request.position()),
                normalizeText(request.layer()),
                SceneStatus.IDEA,
                project
        );

        Scene savedScene = sceneRepository.save(scene);

        activityService.log(
                ownerId,
                ActivityType.SCENE_CREATED,
                ActivityResourceType.SCENE,
                savedScene.getId(),
                savedScene.getTitle(),
                "Created scene \"" + savedScene.getTitle() + "\""
        );

        return SceneResponse.fromEntity(savedScene);
    }

    @Transactional(readOnly = true)
    public List<SceneResponse> findAllByOwnerId(
            UUID ownerId,
            SceneStatus status,
            String layer
    ) {
        return sceneRepository.findByOwnerWithFilters(
                        ownerId,
                        status,
                        normalizeFilter(layer)
                )
                .stream()
                .map(SceneResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SceneResponse> findByProjectId(
            UUID projectId,
            UUID ownerId,
            SceneStatus status,
            String layer
    ) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

        ensureProjectBelongsToUser(project, ownerId);

        return sceneRepository.findByProjectWithFilters(
                        projectId,
                        ownerId,
                        status,
                        normalizeFilter(layer)
                )
                .stream()
                .map(SceneResponse::fromEntity)
                .toList();
    }

    @Transactional
    public SceneResponse update(UUID sceneId, UpdateSceneRequest request, UUID ownerId) {
        Scene scene = sceneRepository.findById(sceneId)
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        ensureSceneBelongsToUser(scene, ownerId);

        if (request.title() != null) {
            updateTitle(scene, request.title());
        }

        if (request.summary() != null) {
            scene.setSummary(normalizeText(request.summary()));
        }

        if (request.position() != null) {
            scene.setPosition(normalizePosition(request.position()));
        }

        if (request.layer() != null) {
            scene.setLayer(normalizeText(request.layer()));
        }

        Scene updatedScene = sceneRepository.save(scene);

        activityService.log(
                ownerId,
                ActivityType.SCENE_UPDATED,
                ActivityResourceType.SCENE,
                updatedScene.getId(),
                updatedScene.getTitle(),
                "Updated scene \"" + updatedScene.getTitle() + "\""
        );

        return SceneResponse.fromEntity(updatedScene);
    }

    @Transactional
    public SceneResponse updateStatus(UUID sceneId, UpdateSceneStatusRequest request, UUID ownerId) {
        Scene scene = sceneRepository.findById(sceneId)
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        ensureSceneBelongsToUser(scene, ownerId);

        scene.setStatus(request.status());

        Scene updatedScene = sceneRepository.save(scene);

        activityService.log(
                ownerId,
                ActivityType.SCENE_STATUS_UPDATED,
                ActivityResourceType.SCENE,
                updatedScene.getId(),
                updatedScene.getTitle(),
                "Changed scene \"" + updatedScene.getTitle() + "\" status to " + updatedScene.getStatus()
        );

        return SceneResponse.fromEntity(updatedScene);
    }

    private void updateTitle(Scene scene, String title) {
        if (title.isBlank()) {
            throw new IllegalArgumentException("Scene title cannot be blank");
        }

        String normalizedTitle = title.trim();

        boolean titleAlreadyExists = sceneRepository
                .existsByTitleIgnoreCaseAndProjectIdAndIdNot(
                        normalizedTitle,
                        scene.getProject().getId(),
                        scene.getId()
                );

        if (titleAlreadyExists) {
            throw new ConflictException("Scene title is already in use for this project");
        }

        scene.setTitle(normalizedTitle);
    }

    private void ensureProjectBelongsToUser(Project project, UUID ownerId) {
        UUID projectOwnerId = project.getWorkspace().getOwner().getId();

        if (!projectOwnerId.equals(ownerId)) {
            throw new ForbiddenException("You do not have permission to access this project");
        }
    }

    private void ensureSceneBelongsToUser(Scene scene, UUID ownerId) {
        UUID sceneOwnerId = scene.getProject().getWorkspace().getOwner().getId();

        if (!sceneOwnerId.equals(ownerId)) {
            throw new ForbiddenException("You do not have permission to access this scene");
        }
    }

    private String normalizeText(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }

        return text.trim();
    }

    private Integer normalizePosition(Integer position) {
        if (position == null) {
            return 0;
        }

        return position;
    }

    private String normalizeFilter(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim().toLowerCase(Locale.ROOT);
    }

    @Transactional(readOnly = true)
    public SceneResponse findById(UUID sceneId, UUID ownerId) {
        Scene scene = sceneRepository.findById(sceneId)
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        ensureSceneBelongsToUser(scene, ownerId);

        return SceneResponse.fromEntity(scene);
    }
}
