package com.nexora.dto;

import java.math.BigDecimal;

public class DashboardMetricsDTO {
    private Long id;
    private Integer totalProjects;
    private Integer totalRepositories;
    private Integer developerCount;
    private BigDecimal knowledgeCoverage;
    private BigDecimal documentationScore;
    private BigDecimal architectureStability;
    private BigDecimal knowledgeRisk;
    private BigDecimal projectHealth;
    private Integer activeContributors;
    private Integer recentCommits;

    // Constructor
    public DashboardMetricsDTO(Long id, Integer totalProjects, Integer totalRepositories,
                               Integer developerCount, BigDecimal knowledgeCoverage,
                               BigDecimal documentationScore, BigDecimal architectureStability,
                               BigDecimal knowledgeRisk, BigDecimal projectHealth,
                               Integer activeContributors, Integer recentCommits) {
        this.id = id;
        this.totalProjects = totalProjects;
        this.totalRepositories = totalRepositories;
        this.developerCount = developerCount;
        this.knowledgeCoverage = knowledgeCoverage;
        this.documentationScore = documentationScore;
        this.architectureStability = architectureStability;
        this.knowledgeRisk = knowledgeRisk;
        this.projectHealth = projectHealth;
        this.activeContributors = activeContributors;
        this.recentCommits = recentCommits;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Integer getTotalProjects() { return totalProjects; }
    public void setTotalProjects(Integer totalProjects) { this.totalProjects = totalProjects; }

    public Integer getTotalRepositories() { return totalRepositories; }
    public void setTotalRepositories(Integer totalRepositories) { this.totalRepositories = totalRepositories; }

    public Integer getDeveloperCount() { return developerCount; }
    public void setDeveloperCount(Integer developerCount) { this.developerCount = developerCount; }

    public BigDecimal getKnowledgeCoverage() { return knowledgeCoverage; }
    public void setKnowledgeCoverage(BigDecimal knowledgeCoverage) { this.knowledgeCoverage = knowledgeCoverage; }

    public BigDecimal getDocumentationScore() { return documentationScore; }
    public void setDocumentationScore(BigDecimal documentationScore) { this.documentationScore = documentationScore; }

    public BigDecimal getArchitectureStability() { return architectureStability; }
    public void setArchitectureStability(BigDecimal architectureStability) { this.architectureStability = architectureStability; }

    public BigDecimal getKnowledgeRisk() { return knowledgeRisk; }
    public void setKnowledgeRisk(BigDecimal knowledgeRisk) { this.knowledgeRisk = knowledgeRisk; }

    public BigDecimal getProjectHealth() { return projectHealth; }
    public void setProjectHealth(BigDecimal projectHealth) { this.projectHealth = projectHealth; }

    public Integer getActiveContributors() { return activeContributors; }
    public void setActiveContributors(Integer activeContributors) { this.activeContributors = activeContributors; }

    public Integer getRecentCommits() { return recentCommits; }
    public void setRecentCommits(Integer recentCommits) { this.recentCommits = recentCommits; }
}
