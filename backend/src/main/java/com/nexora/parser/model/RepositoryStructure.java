package com.nexora.parser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepositoryStructure {
    private String repositoryName;
    private String repositoryUrl;
    private String primaryLanguage;
    private List<String> fileStructure;
    private List<ClassInfo> classes;
    private List<CodeMetrics> fileMetrics;
    private RepositoryMetrics overallMetrics;
}
