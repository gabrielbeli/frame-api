package com.frame.api.reference.controller;

import com.frame.api.reference.dto.CreateSceneReferenceRequest;
import com.frame.api.reference.dto.SceneReferenceResponse;
import com.frame.api.reference.dto.UpdateSceneReferenceRequest;
import com.frame.api.reference.service.SceneReferenceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/references")
public class SceneReferenceController {

    private final SceneReferenceService referenceService;

    public SceneReferenceController(SceneReferenceService referenceService) {
        this.referenceService = referenceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SceneReferenceResponse create(
            @RequestBody @Valid CreateSceneReferenceRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return referenceService.create(request, ownerId);
    }

    @GetMapping
    public List<SceneReferenceResponse> findAll(@AuthenticationPrincipal Jwt jwt) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return referenceService.findAllByOwnerId(ownerId);
    }

    @GetMapping("/{referenceId}")
    public SceneReferenceResponse findById(
            @PathVariable UUID referenceId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return referenceService.findById(referenceId, ownerId);
    }

    @GetMapping("/scene/{sceneId}")
    public List<SceneReferenceResponse> findBySceneId(
            @PathVariable UUID sceneId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return referenceService.findBySceneId(sceneId, ownerId);
    }

    @PatchMapping("/{referenceId}")
    public SceneReferenceResponse update(
            @PathVariable UUID referenceId,
            @RequestBody @Valid UpdateSceneReferenceRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return referenceService.update(referenceId, request, ownerId);
    }
}
