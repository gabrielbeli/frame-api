package com.frame.api.scene.repository;

import com.frame.api.scene.entity.Scene;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SceneRepository extends JpaRepository<Scene,UUID> {

    List<Scene> findByProjectIdOrderByPositionAsc(UUID projectId);

    boolean existsByTitleIgnoreCaseAndProjectId(String title, UUID projectId);

    boolean existsByTitleIgnoreCaseAndProjectIdAndIdNot(
            String title,
            UUID projectId,
            UUID id
    );
}
