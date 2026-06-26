package com.frame.api.activity.entity;

import com.frame.api.user.entity.FrameUser;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "activities")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id", nullable = false)
    private FrameUser actor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private ActivityType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private ActivityResourceType resourceType;

    @Column(nullable = false)
    private UUID resourceId;

    @Column(nullable = false, length = 200)
    private String resourceTitle;

    @Column(nullable = false, length = 500)
    private String message;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    public Activity() {
    }

    public Activity(
            FrameUser actor,
            ActivityType type,
            ActivityResourceType resourceType,
            UUID resourceId,
            String resourceTitle,
            String message
    ) {
        this.actor = actor;
        this.type = type;
        this.resourceType = resourceType;
        this.resourceId = resourceId;
        this.resourceTitle = resourceTitle;
        this.message = message;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public FrameUser getActor() {
        return actor;
    }

    public ActivityType getType() {
        return type;
    }

    public ActivityResourceType getResourceType() {
        return resourceType;
    }

    public UUID getResourceId() {
        return resourceId;
    }

    public String getResourceTitle() {
        return resourceTitle;
    }

    public String getMessage() {
        return message;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setActor(FrameUser actor) {
        this.actor = actor;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public void setResourceType(ActivityResourceType resourceType) {
        this.resourceType = resourceType;
    }

    public void setResourceId(UUID resourceId) {
        this.resourceId = resourceId;
    }

    public void setResourceTitle(String resourceTitle) {
        this.resourceTitle = resourceTitle;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}
