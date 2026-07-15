package com.nexora.git;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepositoryInfo {
    private String repositoryName;
    private String repositoryUrl;
    private String branch;
    private int commitCount;
    private int contributorCount;
    private List<String> languages;
    private String primaryLanguage;
    private long lastCommitTime;
    private String description;
}
