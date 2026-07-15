package com.nexora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepositoryAnalysisRequestDTO {
    private String repositoryUrl;
    private String repositoryName;
    private String branch;
    private boolean includeTests;
    private boolean analyzeAsync;
}
