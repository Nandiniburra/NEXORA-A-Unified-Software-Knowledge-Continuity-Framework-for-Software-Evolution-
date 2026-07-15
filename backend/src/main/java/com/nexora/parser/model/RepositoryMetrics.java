package com.nexora.parser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepositoryMetrics {
    private int totalFiles;
    private int totalLines;
    private int totalClasses;
    private int totalMethods;
    private double averageCyclomaticComplexity;
    private double averageMaintainabilityIndex;
    private int externalDependencies;
    private int internalDependencies;
    private double testCoverage;
    private String architecturePattern;
}
