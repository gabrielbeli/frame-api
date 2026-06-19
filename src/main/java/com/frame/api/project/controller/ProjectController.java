package com.frame.api.project.controller;

import com.frame.api.project.dto.CreateProjectRequest;
import com.frame.api.project.dto.ProjectResponse;
import com.frame.api.project.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ProjectResponse create(@RequestBody @Valid CreateProjectRequest request) {
        return projectService.create(request);
    }

    @GetMapping
    public List<ProjectResponse> findAll() {
        return projectService.findAll();
    }

    @GetMapping("/workspace/{workspaceId}")
    public List<ProjectResponse> findByWorkspaceId(@PathVariable UUID workspaceId) {
        return projectService.findByWorkspaceId(workspaceId);
    }
}
