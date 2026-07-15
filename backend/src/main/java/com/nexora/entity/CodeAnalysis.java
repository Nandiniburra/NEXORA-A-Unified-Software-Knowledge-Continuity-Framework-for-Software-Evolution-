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
@Table(name = "code_analysis")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "repository_id", nullable = false)
    private Repository repository;

    @Column(name = "total_files")
    private Integer totalFiles;

    @Column(name = "total_lines")
    private Integer totalLines;

    @Column(name = "total_classes")
    private Integer totalClasses;

    @Column(name = "total_methods")
    private Integer totalMethods;

    @Column(name = "average_complexity", precision = 5, scale = 2)
    private Double averageComplexity;

    @Column(name = "average_maintainability", precision = 5, scale = 2)
    private Double averageMaintainability;

    @Column(name = "architecture_pattern")
    private String architecturePattern;

    @Column(name = "external_dependencies")
    private Integer externalDependencies;

    @Column(name = "internal_dependencies")
    private Integer internalDependencies;

    @Column(columnDefinition = "TEXT")
    private String analysisDetails;

    @Column(name = "analysis_status")
    private String analysisStatus; // PENDING, SUCCESS, FAILED

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
