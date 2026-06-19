package com.frame.api.workspace.controller;

import com.frame.api.workspace.dto.CreateWorkspaceRequest;
import com.frame.api.workspace.dto.WorkspaceResponse;
import com.frame.api.workspace.service.WorkspaceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public WorkspaceResponse create(@RequestBody @Valid CreateWorkspaceRequest request) {
        return workspaceService.create(request);
    }

    @GetMapping
    public List<WorkspaceResponse> findAll() {
        return workspaceService.findAll();
    }

    @GetMapping("/owner/{ownerId}")
    public List<WorkspaceResponse> findByOwnerId(@PathVariable UUID ownerId) {
        return workspaceService.findByOwnerId(ownerId);
    }
}
