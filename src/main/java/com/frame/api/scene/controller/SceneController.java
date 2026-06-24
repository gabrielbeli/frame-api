package com.frame.api.scene.controller;

import com.frame.api.scene.dto.CreateSceneRequest;
import com.frame.api.scene.dto.SceneResponse;
import com.frame.api.scene.dto.UpdateSceneRequest;
import com.frame.api.scene.dto.UpdateSceneStatusRequest;
import com.frame.api.scene.service.SceneService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/scenes")
public class SceneController {

    private final SceneService sceneService;

    public SceneController(SceneService sceneService) {
        this.sceneService = sceneService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SceneResponse create(
            @RequestBody @Valid CreateSceneRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return sceneService.create(request, ownerId);
    }

    @GetMapping
    public List<SceneResponse> findAll(@AuthenticationPrincipal Jwt jwt) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return sceneService.findAllByOwnerId(ownerId);
    }

    @GetMapping("/project/{projectId}")
    public List<SceneResponse> findByProjectId(
            @PathVariable UUID projectId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return sceneService.findByProjectId(projectId, ownerId);
    }

    @PatchMapping("/{sceneId}")
    public SceneResponse update(
            @PathVariable UUID sceneId,
            @RequestBody @Valid UpdateSceneRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return sceneService.update(sceneId, request, ownerId);
    }

    @PatchMapping("/{sceneId}/status")
    public SceneResponse updateStatus(
            @PathVariable UUID sceneId,
            @RequestBody @Valid UpdateSceneStatusRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return sceneService.updateStatus(sceneId, request, ownerId);
    }
}
