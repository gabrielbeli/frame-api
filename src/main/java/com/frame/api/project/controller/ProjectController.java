package com.frame.api.project.controller;

import com.frame.api.project.dto.CreateProjectRequest;
import com.frame.api.project.dto.ProjectResponse;
import com.frame.api.project.dto.UpdateProjectRequest;
import com.frame.api.project.dto.UpdateProjectStatusRequest;
import com.frame.api.project.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import com.frame.api.project.entity.ProjectStatus;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse create(
            @RequestBody @Valid CreateProjectRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return projectService.create(request, ownerId);
    }

    @GetMapping
    public List<ProjectResponse> findAll(
            @RequestParam(required = false) ProjectStatus status,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return projectService.findAllByOwnerId(ownerId, status);
    }

    @GetMapping("/{projectId}")
    public ProjectResponse findById(
            @PathVariable UUID projectId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return projectService.findById(projectId, ownerId);
    }

    @GetMapping("/workspace/{workspaceId}")
    public List<ProjectResponse> findByWorkspaceId(
            @PathVariable UUID workspaceId,
            @RequestParam(required = false) ProjectStatus status,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return projectService.findByWorkspaceId(workspaceId, ownerId, status);
    }

    @PatchMapping("/{projectId}")
    public ProjectResponse update(
            @PathVariable UUID projectId,
            @RequestBody @Valid UpdateProjectRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return projectService.update(projectId, request, ownerId);
    }

    @PatchMapping("/{projectId}/status")
    public ProjectResponse updateStatus(
            @PathVariable UUID projectId,
            @RequestBody @Valid UpdateProjectStatusRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return projectService.updateStatus(projectId, request, ownerId);
    }
}
