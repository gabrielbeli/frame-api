package com.frame.api.scene.entity;

import com.frame.api.project.entity.Project;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "scenes")
public class Scene {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 160)
    private String title;

    @Column(length = 1000)
    private String summary;

    @Column(nullable = false)
    private Integer position;

    @Column(length = 80)
    private String layer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SceneStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    public Scene() {
    }

    public Scene(
            String title,
            String summary,
            Integer position,
            String layer,
            SceneStatus status,
            Project project
    ) {
        this.title = title;
        this.summary = summary;
        this.position = position;
        this.layer = layer;
        this.status = status;
        this.project = project;
    }

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null) {
            this.status = SceneStatus.IDEA;
        }

        if (this.position == null) {
            this.position = 0;
        }
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

    public String getSummary() {
        return summary;
    }

    public Integer getPosition() {
        return position;
    }

    public String getLayer() {
        return layer;
    }

    public SceneStatus getStatus() {
        return status;
    }

    public Project getProject() {
        return project;
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

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public void setStatus(SceneStatus status) {
        this.status = status;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
