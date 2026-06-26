package com.frame.api.activity.service;

import com.frame.api.activity.dto.ActivityResponse;
import com.frame.api.activity.entity.Activity;
import com.frame.api.activity.entity.ActivityResourceType;
import com.frame.api.activity.entity.ActivityType;
import com.frame.api.activity.repository.ActivityRepository;
import com.frame.api.common.exception.ResourceNotFoundException;
import com.frame.api.user.entity.FrameUser;
import com.frame.api.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;

    public ActivityService(
            ActivityRepository activityRepository,
            UserRepository userRepository
    ) {
        this.activityRepository = activityRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void log(
            UUID actorId,
            ActivityType type,
            ActivityResourceType resourceType,
            UUID resourceId,
            String resourceTitle,
            String message
    ) {
        FrameUser actor = userRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Activity activity = new Activity(
                actor,
                type,
                resourceType,
                resourceId,
                resourceTitle,
                message
        );

        activityRepository.save(activity);
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> findRecent(UUID actorId) {
        return activityRepository.findTop20ByActorIdOrderByCreatedAtDesc(actorId)
                .stream()
                .map(ActivityResponse::fromEntity)
                .toList();
    }
}
