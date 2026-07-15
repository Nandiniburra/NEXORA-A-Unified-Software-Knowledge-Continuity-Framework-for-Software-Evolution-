package com.nexora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepositoryStructureDTO {
    private String repositoryName;
    private String repositoryPath;
    private Map<String, Integer> fileTypeDistribution;
    private long totalSize;
    private int totalFiles;
    private int totalDirectories;
    private List<String> javaFiles;
    private List<String> configFiles;
    private List<String> testFiles;
}
