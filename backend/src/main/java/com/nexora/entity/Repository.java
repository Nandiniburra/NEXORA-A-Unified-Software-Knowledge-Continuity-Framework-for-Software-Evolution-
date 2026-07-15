package com.nexora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "repositories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Repository {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @Column(length = 100)
    private String branch = "main";

    @Column(name = "commit_count")
    private Integer commitCount = 0;

    @Column(name = "contributor_count")
    private Integer contributorCount = 0;

    @Column(name = "last_analyzed")
    private LocalDateTime lastAnalyzed;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
