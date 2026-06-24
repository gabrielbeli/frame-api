package com.frame.api.workspace.controller;

import com.frame.api.workspace.dto.CreateWorkspaceRequest;
import com.frame.api.workspace.dto.WorkspaceResponse;
import com.frame.api.workspace.service.WorkspaceService;
import com.frame.api.workspace.dto.UpdateWorkspaceRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkspaceResponse create(@RequestBody @Valid CreateWorkspaceRequest request, @AuthenticationPrincipal Jwt jwt) {

        UUID ownerId = UUID.fromString(jwt.getSubject());

        return workspaceService.create(request, ownerId);
    }

    @GetMapping
    public List<WorkspaceResponse> findAll(@AuthenticationPrincipal Jwt jwt) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return workspaceService.findByOwnerId(ownerId);
    }

    @GetMapping("/{workspaceId}")
    public WorkspaceResponse findById(
            @PathVariable UUID workspaceId,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return workspaceService.findById(workspaceId, ownerId);
    }

    @PatchMapping("/{workspaceId}")
    public WorkspaceResponse update(
            @PathVariable UUID workspaceId,
            @RequestBody @Valid UpdateWorkspaceRequest request,
            @AuthenticationPrincipal Jwt jwt
    ) {
        UUID ownerId = UUID.fromString(jwt.getSubject());

        return workspaceService.update(workspaceId, request, ownerId);
    }
}
