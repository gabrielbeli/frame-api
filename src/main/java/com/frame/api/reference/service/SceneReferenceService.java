package com.frame.api.reference.service;

import com.frame.api.common.exception.ConflictException;
import com.frame.api.common.exception.ForbiddenException;
import com.frame.api.common.exception.ResourceNotFoundException;
import com.frame.api.reference.dto.CreateSceneReferenceRequest;
import com.frame.api.reference.dto.SceneReferenceResponse;
import com.frame.api.reference.dto.UpdateSceneReferenceRequest;
import com.frame.api.reference.entity.SceneReference;
import com.frame.api.reference.repository.SceneReferenceRepository;
import com.frame.api.scene.entity.Scene;
import com.frame.api.scene.repository.SceneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class SceneReferenceService {

    private final SceneReferenceRepository referenceRepository;
    private final SceneRepository sceneRepository;

    public SceneReferenceService(
            SceneReferenceRepository referenceRepository,
            SceneRepository sceneRepository
    ) {
        this.referenceRepository = referenceRepository;
        this.sceneRepository = sceneRepository;
    }

    @Transactional
    public SceneReferenceResponse create(
            CreateSceneReferenceRequest request,
            UUID ownerId
    ) {
        Scene scene = sceneRepository.findById(request.sceneId())
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        ensureSceneBelongsToUser(scene, ownerId);

        String normalizedTitle = request.title().trim();

        boolean referenceAlreadyExists = referenceRepository
                .existsByTitleIgnoreCaseAndSceneId(normalizedTitle, scene.getId());

        if (referenceAlreadyExists) {
            throw new ConflictException("Reference title is already in use for this scene");
        }

        SceneReference reference = new SceneReference(
                normalizedTitle,
                normalizeText(request.description()),
                normalizeText(request.url()),
                request.type(),
                scene
        );

        SceneReference savedReference = referenceRepository.save(reference);

        return SceneReferenceResponse.fromEntity(savedReference);
    }

    @Transactional(readOnly = true)
    public List<SceneReferenceResponse> findAllByOwnerId(UUID ownerId) {
        return referenceRepository
                .findByScene_Project_Workspace_Owner_IdOrderByUpdatedAtDesc(ownerId)
                .stream()
                .map(SceneReferenceResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public SceneReferenceResponse findById(UUID referenceId, UUID ownerId) {
        SceneReference reference = referenceRepository.findById(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Reference not found"));

        ensureReferenceBelongsToUser(reference, ownerId);

        return SceneReferenceResponse.fromEntity(reference);
    }

    @Transactional(readOnly = true)
    public List<SceneReferenceResponse> findBySceneId(UUID sceneId, UUID ownerId) {
        Scene scene = sceneRepository.findById(sceneId)
                .orElseThrow(() -> new ResourceNotFoundException("Scene not found"));

        ensureSceneBelongsToUser(scene, ownerId);

        return referenceRepository.findBySceneIdOrderByUpdatedAtDesc(sceneId)
                .stream()
                .map(SceneReferenceResponse::fromEntity)
                .toList();
    }

    @Transactional
    public SceneReferenceResponse update(
            UUID referenceId,
            UpdateSceneReferenceRequest request,
            UUID ownerId
    ) {
        SceneReference reference = referenceRepository.findById(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Reference not found"));

        ensureReferenceBelongsToUser(reference, ownerId);

        if (request.title() != null) {
            updateTitle(reference, request.title());
        }

        if (request.description() != null) {
            reference.setDescription(normalizeText(request.description()));
        }

        if (request.url() != null) {
            reference.setUrl(normalizeText(request.url()));
        }

        if (request.type() != null) {
            reference.setType(request.type());
        }

        SceneReference updatedReference = referenceRepository.save(reference);

        return SceneReferenceResponse.fromEntity(updatedReference);
    }

    private void updateTitle(SceneReference reference, String title) {
        if (title.isBlank()) {
            throw new IllegalArgumentException("Reference title cannot be blank");
        }

        String normalizedTitle = title.trim();

        boolean titleAlreadyExists = referenceRepository
                .existsByTitleIgnoreCaseAndSceneIdAndIdNot(
                        normalizedTitle,
                        reference.getScene().getId(),
                        reference.getId()
                );

        if (titleAlreadyExists) {
            throw new ConflictException("Reference title is already in use for this scene");
        }

        reference.setTitle(normalizedTitle);
    }

    private void ensureSceneBelongsToUser(Scene scene, UUID ownerId) {
        UUID sceneOwnerId = scene.getProject().getWorkspace().getOwner().getId();

        if (!sceneOwnerId.equals(ownerId)) {
            throw new ForbiddenException("You do not have permission to access this scene");
        }
    }

    private void ensureReferenceBelongsToUser(SceneReference reference, UUID ownerId) {
        UUID referenceOwnerId = reference
                .getScene()
                .getProject()
                .getWorkspace()
                .getOwner()
                .getId();

        if (!referenceOwnerId.equals(ownerId)) {
            throw new ForbiddenException("You do not have permission to access this reference");
        }
    }

    private String normalizeText(String text) {
        if (text == null || text.isBlank()) {
            return null;
        }

        return text.trim();
    }
}
