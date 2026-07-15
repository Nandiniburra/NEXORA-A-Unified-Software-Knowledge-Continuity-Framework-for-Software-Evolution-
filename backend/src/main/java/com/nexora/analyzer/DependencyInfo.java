package com.nexora.analyzer;

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
public class DependencyInfo {
    private String dependencyName;
    private String version;
    private String type; // COMPILE, RUNTIME, TEST
    private String source; // MAVEN, GRADLE, NPM, etc.
    private List<String> transitiveDependencies;
}
