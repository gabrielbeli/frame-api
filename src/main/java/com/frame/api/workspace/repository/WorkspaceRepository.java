package com.frame.api.workspace.repository;

import com.frame.api.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {

    List<Workspace> findByOwnerId(UUID ownerId);

    long countByOwnerId(UUID ownerId);

    boolean existsByNameIgnoreCaseAndOwnerId(String name, UUID ownerId);

    boolean existsByNameIgnoreCaseAndOwnerIdAndIdNot(
            String name,
            UUID ownerId,
            UUID id
    );
}
