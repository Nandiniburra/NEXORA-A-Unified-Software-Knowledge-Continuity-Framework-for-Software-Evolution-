package com.nexora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "dashboard_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardMetrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "total_projects")
    private Integer totalProjects;

    @Column(name = "total_repositories")
    private Integer totalRepositories;

    @Column(name = "developer_count")
    private Integer developerCount;

    @Column(name = "knowledge_coverage", precision = 5, scale = 2)
    private BigDecimal knowledgeCoverage;

    @Column(name = "documentation_score", precision = 5, scale = 2)
    private BigDecimal documentationScore;

    @Column(name = "architecture_stability", precision = 5, scale = 2)
    private BigDecimal architectureStability;

    @Column(name = "knowledge_risk", precision = 5, scale = 2)
    private BigDecimal knowledgeRisk;

    @Column(name = "project_health", precision = 5, scale = 2)
    private BigDecimal projectHealth;

    @Column(name = "active_contributors")
    private Integer activeContributors;

    @Column(name = "recent_commits")
    private Integer recentCommits;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
