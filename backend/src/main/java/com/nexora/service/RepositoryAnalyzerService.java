package com.nexora.service;

import com.nexora.analyzer.DependencyExtractor;
import com.nexora.analyzer.DependencyInfo;
import com.nexora.analyzer.FileExplorer;
import com.nexora.dto.CodeAnalysisResultDTO;
import com.nexora.git.GitRepositoryCloner;
import com.nexora.git.RepositoryInfo;
import com.nexora.parser.ArchitectureDetector;
import com.nexora.parser.CodeMetricsCalculator;
import com.nexora.parser.JavaCodeParser;
import com.nexora.parser.model.ClassInfo;
import com.nexora.parser.model.CodeMetrics;
import com.nexora.parser.model.RepositoryMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class RepositoryAnalyzerService {

    @Autowired
    private GitRepositoryCloner gitRepositoryCloner;

    @Autowired
    private JavaCodeParser javaCodeParser;

    @Autowired
    private CodeMetricsCalculator metricsCalculator;

    @Autowired
    private ArchitectureDetector architectureDetector;

    @Autowired
    private FileExplorer fileExplorer;

    @Autowired
    private DependencyExtractor dependencyExtractor;

    public CodeAnalysisResultDTO analyzeRepositoryFromUrl(String repositoryUrl, String repositoryName, Long repositoryId) {
        log.info("Starting repository analysis from URL: {}", repositoryUrl);
        
        String clonedPath = null;
        try {
            // Clone repository
            clonedPath = gitRepositoryCloner.cloneRepository(repositoryUrl, repositoryName);
            log.info("Repository cloned successfully to: {}", clonedPath);

            // Analyze cloned repository
            return analyzeRepository(clonedPath, repositoryName, repositoryId);
            
        } catch (IOException | InterruptedException e) {
            log.error("Failed to clone repository: {}", repositoryUrl, e);
            return buildFailureResult(repositoryName, repositoryId, "Clone failed: " + e.getMessage());
        } finally {
            // Cleanup
            if (clonedPath != null) {
                try {
                    gitRepositoryCloner.cleanupRepository(clonedPath);
                    log.info("Cleaned up temporary repository");
                } catch (IOException e) {
                    log.warn("Failed to cleanup temporary repository", e);
                }
            }
        }
    }

    public CodeAnalysisResultDTO analyzeRepository(String repositoryPath, String repositoryName, Long repositoryId) {
        log.info("Starting detailed repository analysis: {}", repositoryName);
        
        try {
            Path repoPath = Paths.get(repositoryPath);
            
            // Extract classes and code structure
            List<ClassInfo> classes = extractClasses(repoPath);
            
            // Calculate file metrics
            List<CodeMetrics> fileMetrics = calculateFileMetrics(repoPath);
            
            // Detect architecture
            String architecture = architectureDetector.detectArchitecturePattern(repoPath);
            
            // Extract dependencies
            List<DependencyInfo> dependencies = dependencyExtractor.extractAllDependencies(repositoryPath);
            
            // Calculate overall metrics
            RepositoryMetrics overallMetrics = calculateOverallMetrics(classes, fileMetrics, architecture, dependencies);
            
            // Get repository structure
            Map<String, Object> structure = fileExplorer.getDirectoryStructure(repositoryPath);
            
            return CodeAnalysisResultDTO.builder()
                    .repositoryId(repositoryId)
                    .repositoryName(repositoryName)
                    .architecturePattern(architecture)
                    .classes(classes)
                    .fileMetrics(fileMetrics)
                    .overallMetrics(overallMetrics)
                    .analysisStatus("SUCCESS")
                    .analysisTimestamp(LocalDateTime.now().toString())
                    .build();
                    
        } catch (Exception e) {
            log.error("Error analyzing repository: {}", repositoryName, e);
            return buildFailureResult(repositoryName, repositoryId, e.getMessage());
        }
    }

    public RepositoryInfo getRepositoryInfo(String repositoryPath) {
        try {
            String metadata = gitRepositoryCloner.getRepositoryMetadata(repositoryPath);
            
            return RepositoryInfo.builder()
                    .repositoryName(Paths.get(repositoryPath).getFileName().toString())
                    .build();
        } catch (IOException | InterruptedException e) {
            log.error("Failed to get repository info", e);
            return null;
        }
    }

    private List<ClassInfo> extractClasses(Path repositoryPath) throws IOException {
        List<ClassInfo> classes = new ArrayList<>();
        List<Path> javaFiles = Files.walk(repositoryPath)
                .filter(p -> p.toString().endsWith(".java"))
                .toList();

        for (Path javaFile : javaFiles) {
            try {
                ClassInfo classInfo = javaCodeParser.parseJavaFile(javaFile);
                classes.add(classInfo);
            } catch (Exception e) {
                log.warn("Failed to parse file: {}", javaFile, e);
            }
        }

        return classes;
    }

    private List<CodeMetrics> calculateFileMetrics(Path repositoryPath) throws IOException {
        List<CodeMetrics> metrics = new ArrayList<>();
        List<Path> javaFiles = Files.walk(repositoryPath)
                .filter(p -> p.toString().endsWith(".java"))
                .toList();

        for (Path javaFile : javaFiles) {
            try {
                CodeMetrics fileMetrics = metricsCalculator.calculateMetrics(javaFile);
                metrics.add(fileMetrics);
            } catch (Exception e) {
                log.warn("Failed to calculate metrics for file: {}", javaFile, e);
            }
        }

        return metrics;
    }

    private RepositoryMetrics calculateOverallMetrics(List<ClassInfo> classes, 
                                                      List<CodeMetrics> fileMetrics,
                                                      String architecture,
                                                      List<DependencyInfo> dependencies) {
        RepositoryMetrics metrics = RepositoryMetrics.builder()
                .totalFiles(fileMetrics.size())
                .totalClasses(classes.size())
                .totalMethods((int) classes.stream().mapToLong(c -> c.getMethods().size()).sum())
                .architecturePattern(architecture)
                .build();

        // Calculate totals
        metrics.setTotalLines(fileMetrics.stream().mapToInt(CodeMetrics::getTotalLines).sum());
        metrics.setAverageCyclomaticComplexity(
                fileMetrics.stream().mapToDouble(CodeMetrics::getCyclomaticComplexity).average().orElse(1.0)
        );
        metrics.setAverageMaintainabilityIndex(
                fileMetrics.stream().mapToDouble(CodeMetrics::getMaintainabilityIndex).average().orElse(50.0)
        );

        // Count dependencies
        long externalDeps = dependencies.stream()
                .filter(d -> !d.getDependencyName().startsWith("com.nexora"))
                .distinct()
                .count();
        
        long internalDeps = dependencies.stream()
                .filter(d -> d.getDependencyName().startsWith("com.nexora"))
                .distinct()
                .count();
        
        metrics.setExternalDependencies((int) externalDeps);
        metrics.setInternalDependencies((int) internalDeps);

        return metrics;
    }

    private CodeAnalysisResultDTO buildFailureResult(String repositoryName, Long repositoryId, String errorMessage) {
        return CodeAnalysisResultDTO.builder()
                .repositoryId(repositoryId)
                .repositoryName(repositoryName)
                .analysisStatus("FAILED")
                .analysisTimestamp(LocalDateTime.now().toString())
                .build();
    }
}
