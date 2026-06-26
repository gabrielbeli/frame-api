package com.frame.api.activity.dto;

import com.frame.api.activity.entity.Activity;
import com.frame.api.activity.entity.ActivityResourceType;
import com.frame.api.activity.entity.ActivityType;

import java.time.Instant;
import java.util.UUID;

public record ActivityResponse(
        UUID id,
        ActivityType type,
        ActivityResourceType resourceType,
        UUID resourceId,
        String resourceTitle,
        String message,
        UUID actorId,
        String actorName,
        Instant createdAt
) {
    public static ActivityResponse fromEntity(Activity activity) {
        return new ActivityResponse(
                activity.getId(),
                activity.getType(),
                activity.getResourceType(),
                activity.getResourceId(),
                activity.getResourceTitle(),
                activity.getMessage(),
                activity.getActor().getId(),
                activity.getActor().getFullName(),
                activity.getCreatedAt()
        );
    }
}
