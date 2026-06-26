package com.frame.api.activity.repository;

import com.frame.api.activity.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {

    List<Activity> findTop20ByActorIdOrderByCreatedAtDesc(UUID actorId);
}
