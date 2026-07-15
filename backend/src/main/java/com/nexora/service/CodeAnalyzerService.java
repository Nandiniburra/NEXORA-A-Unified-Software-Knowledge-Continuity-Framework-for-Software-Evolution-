package com.nexora.service;

import com.nexora.dto.CodeAnalysisResultDTO;
import com.nexora.parser.JavaCodeParser;
import com.nexora.parser.CodeMetricsCalculator;
import com.nexora.parser.ArchitectureDetector;
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
public class CodeAnalyzerService {

    @Autowired
    private JavaCodeParser javaCodeParser;

    @Autowired
    private CodeMetricsCalculator metricsCalculator;

    @Autowired
    private ArchitectureDetector architectureDetector;

    public CodeAnalysisResultDTO analyzeRepository(String repositoryPath, String repositoryName, Long repositoryId) {
        log.info("Starting analysis of repository: {}", repositoryName);
        
        try {
            Path repoPath = Paths.get(repositoryPath);
            
            // Extract classes
            List<ClassInfo> classes = extractClasses(repoPath);
            
            // Calculate file metrics
            List<CodeMetrics> fileMetrics = calculateFileMetrics(repoPath);
            
            // Detect architecture
            String architecture = architectureDetector.detectArchitecturePattern(repoPath);
            
            // Calculate overall metrics
            RepositoryMetrics overallMetrics = calculateOverallMetrics(classes, fileMetrics, architecture);
            
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
            return CodeAnalysisResultDTO.builder()
                    .repositoryId(repositoryId)
                    .repositoryName(repositoryName)
                    .analysisStatus("FAILED")
                    .analysisTimestamp(LocalDateTime.now().toString())
                    .build();
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
                                                      String architecture) {
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
        long externalDeps = fileMetrics.stream()
                .flatMap(m -> m.getDependencies().stream())
                .filter(d -> !d.startsWith("com.nexora"))
                .distinct()
                .count();
        
        long internalDeps = fileMetrics.stream()
                .flatMap(m -> m.getDependencies().stream())
                .filter(d -> d.startsWith("com.nexora"))
                .distinct()
                .count();
        
        metrics.setExternalDependencies((int) externalDeps);
        metrics.setInternalDependencies((int) internalDeps);

        return metrics;
    }

    public Map<String, Object> getDetailedAnalysis(Long repositoryId) {
        Map<String, Object> analysis = new HashMap<>();
        // TODO: Fetch from database and format detailed analysis
        analysis.put("status", "pending");
        return analysis;
    }
}
