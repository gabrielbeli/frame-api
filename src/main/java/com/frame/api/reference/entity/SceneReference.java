package com.frame.api.reference.entity;

import com.frame.api.scene.entity.Scene;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "scene_references")
public class SceneReference {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 160)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(length = 1000)
    private String url;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SceneReferenceType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scene_id", nullable = false)
    private Scene scene;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    public SceneReference() {
    }

    public SceneReference(
            String title,
            String description,
            String url,
            SceneReferenceType type,
            Scene scene
    ) {
        this.title = title;
        this.description = description;
        this.url = url;
        this.type = type;
        this.scene = scene;
    }

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public SceneReferenceType getType() {
        return type;
    }

    public Scene getScene() {
        return scene;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(SceneReferenceType type) {
        this.type = type;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
