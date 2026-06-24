package com.frame.api.project.controller;

import com.frame.api.project.dto.CreateProjectRequest;
import com.frame.api.project.dto.ProjectResponse;
import com.frame.api.project.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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
    public List<ProjectResponse> findAll(@AuthenticationPrincipal Jwt jwt) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return projectService.findAllByOwnerId(ownerId);
    }

    @GetMapping("/workspace/{workspaceId}")
    public List<ProjectResponse> findByWorkspaceId(
            @PathVariable UUID workspaceId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return projectService.findByWorkspaceId(workspaceId, ownerId);
    }
}
