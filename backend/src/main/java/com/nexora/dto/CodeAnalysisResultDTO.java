package com.nexora.dto;

import com.nexora.parser.model.ClassInfo;
import com.nexora.parser.model.CodeMetrics;
import com.nexora.parser.model.RepositoryMetrics;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeAnalysisResultDTO {
    private Long repositoryId;
    private String repositoryName;
    private String repositoryUrl;
    private String architecturePattern;
    private List<ClassInfo> classes;
    private List<CodeMetrics> fileMetrics;
    private RepositoryMetrics overallMetrics;
    private String analysisStatus;
    private String analysisTimestamp;
}
