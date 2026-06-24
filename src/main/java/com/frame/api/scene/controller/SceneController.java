package com.frame.api.scene.controller;

import com.frame.api.scene.dto.CreateSceneRequest;
import com.frame.api.scene.dto.SceneResponse;
import com.frame.api.scene.service.SceneService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public SceneResponse create(@RequestBody @Valid CreateSceneRequest request) {
        return sceneService.create(request);
    }

    @GetMapping
    public List<SceneResponse> findAll() {
        return sceneService.findAll();
    }

    @GetMapping("/project/{projectId}")
    public List<SceneResponse> findByProjectId(@PathVariable UUID projectId) {
        return sceneService.findByProjectId(projectId);
    }
}
