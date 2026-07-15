package com.nexora.service;

import com.nexora.dto.DashboardMetricsDTO;
import com.nexora.entity.DashboardMetrics;
import com.nexora.entity.Project;
import com.nexora.repository.DashboardMetricsRepository;
import com.nexora.repository.ProjectRepository;
import com.nexora.repository.RepositoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
public class DashboardService {

    @Autowired
    private DashboardMetricsRepository dashboardMetricsRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private RepositoryRepository repositoryRepository;

    public DashboardMetricsDTO getDashboardMetrics(Long projectId, Long userId) {
        Project project = projectRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Optional<DashboardMetrics> metrics = dashboardMetricsRepository.findByProjectId(projectId);
        
        if (metrics.isPresent()) {
            return convertToDTO(metrics.get());
        }

        // Generate default metrics
        return generateDefaultMetrics(projectId);
    }

    public DashboardMetricsDTO generateDefaultMetrics(Long projectId) {
        long totalProjects = projectRepository.count();
        long totalRepositories = repositoryRepository.count();
        
        return new DashboardMetricsDTO(
                null,
                (int) totalProjects,
                (int) totalRepositories,
                0,
                BigDecimal.valueOf(45.5),      // Knowledge Coverage
                BigDecimal.valueOf(62.3),      // Documentation Score
                BigDecimal.valueOf(78.5),      // Architecture Stability
                BigDecimal.valueOf(34.2),      // Knowledge Risk
                BigDecimal.valueOf(71.0),      // Project Health
                5,                             // Active Contributors
                142                            // Recent Commits
        );
    }

    public DashboardMetricsDTO updateMetrics(Long projectId, DashboardMetricsDTO metricsDTO, Long userId) {
        Project project = projectRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Optional<DashboardMetrics> existingMetrics = dashboardMetricsRepository.findByProjectId(projectId);
        DashboardMetrics metrics;

        if (existingMetrics.isPresent()) {
            metrics = existingMetrics.get();
        } else {
            metrics = new DashboardMetrics();
            metrics.setProject(project);
        }

        metrics.setTotalProjects(metricsDTO.getTotalProjects());
        metrics.setTotalRepositories(metricsDTO.getTotalRepositories());
        metrics.setDeveloperCount(metricsDTO.getDeveloperCount());
        metrics.setKnowledgeCoverage(metricsDTO.getKnowledgeCoverage());
        metrics.setDocumentationScore(metricsDTO.getDocumentationScore());
        metrics.setArchitectureStability(metricsDTO.getArchitectureStability());
        metrics.setKnowledgeRisk(metricsDTO.getKnowledgeRisk());
        metrics.setProjectHealth(metricsDTO.getProjectHealth());
        metrics.setActiveContributors(metricsDTO.getActiveContributors());
        metrics.setRecentCommits(metricsDTO.getRecentCommits());

        dashboardMetricsRepository.save(metrics);
        log.info("Metrics updated for project: {}", projectId);
        return convertToDTO(metrics);
    }

    private DashboardMetricsDTO convertToDTO(DashboardMetrics metrics) {
        return new DashboardMetricsDTO(
                metrics.getId(),
                metrics.getTotalProjects(),
                metrics.getTotalRepositories(),
                metrics.getDeveloperCount(),
                metrics.getKnowledgeCoverage(),
                metrics.getDocumentationScore(),
                metrics.getArchitectureStability(),
                metrics.getKnowledgeRisk(),
                metrics.getProjectHealth(),
                metrics.getActiveContributors(),
                metrics.getRecentCommits()
        );
    }
}
