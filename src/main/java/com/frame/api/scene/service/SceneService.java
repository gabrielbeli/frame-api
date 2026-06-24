package com.frame.api.scene.service;

import com.frame.api.project.entity.Project;
import com.frame.api.project.repository.ProjectRepository;
import com.frame.api.scene.dto.CreateSceneRequest;
import com.frame.api.scene.dto.SceneResponse;
import com.frame.api.scene.entity.Scene;
import com.frame.api.scene.entity.SceneStatus;
import com.frame.api.scene.repository.SceneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.frame.api.common.exception.ConflictException;
import com.frame.api.common.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
public class SceneService {

    private final SceneRepository sceneRepository;
    private final ProjectRepository projectRepository;

    public SceneService(
            SceneRepository sceneRepository,
            ProjectRepository projectRepository
    ) {
        this.sceneRepository = sceneRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public SceneResponse create(CreateSceneRequest request) {
        Project project = projectRepository.findById(request.projectId())
                .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

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

        return SceneResponse.fromEntity(savedScene);
    }

    @Transactional(readOnly = true)
    public List<SceneResponse> findAll() {
        return sceneRepository.findAll()
                .stream()
                .map(SceneResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SceneResponse> findByProjectId(UUID projectId) {
        return sceneRepository.findByProjectIdOrderByPositionAsc(projectId)
                .stream()
                .map(SceneResponse::fromEntity)
                .toList();
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
}
