package com.nexora.parser;

import com.nexora.parser.model.CodeMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

@Component
@Slf4j
public class CodeMetricsCalculator {

    @Autowired
    private JavaCodeParser javaCodeParser;

    public CodeMetrics calculateMetrics(Path filePath) throws IOException {
        String content = Files.readString(filePath);
        return calculateMetrics(content, filePath.getFileName().toString(), filePath.toString());
    }

    public CodeMetrics calculateMetrics(String content, String fileName, String filePath) {
        CodeMetrics metrics = CodeMetrics.builder()
                .fileName(fileName)
                .filePath(filePath)
                .build();

        // Line metrics
        metrics.setTotalLines(countLines(content));
        metrics.setCodeLines(countCodeLines(content));
        metrics.setCommentLines(countCommentLines(content));
        metrics.setBlankLines(metrics.getTotalLines() - metrics.getCodeLines() - metrics.getCommentLines());

        // Code structure metrics
        metrics.setClassCount(countClasses(content));
        metrics.setMethodCount(countMethods(content));
        metrics.setFieldCount(countFields(content));

        // Complexity metrics
        metrics.setCyclomaticComplexity(calculateAverageCyclomaticComplexity(content));
        metrics.setMaintainabilityIndex(calculateMaintainabilityIndex(metrics));

        // Comment ratio
        metrics.setCommentRatio((double) metrics.getCommentLines() / Math.max(metrics.getTotalLines(), 1));

        // Dependencies
        metrics.setDependencies(javaCodeParser.extractImports(content));

        return metrics;
    }

    private int countLines(String content) {
        return content.split("\\n").length;
    }

    private int countCodeLines(String content) {
        String[] lines = content.split("\\n");
        int codeLines = 0;
        for (String line : lines) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("//") && !trimmed.startsWith("*")) {
                codeLines++;
            }
        }
        return codeLines;
    }

    private int countCommentLines(String content) {
        int comments = 0;
        Pattern singleLine = Pattern.compile("//.*$", Pattern.MULTILINE);
        Pattern multiLine = Pattern.compile("/\\*.*?\\*/", Pattern.DOTALL);
        
        comments += singleLine.matcher(content).results().count();
        comments += multiLine.matcher(content).results().count();
        
        return (int) comments;
    }

    private int countClasses(String content) {
        Pattern pattern = Pattern.compile("\\b(class|interface|enum)\\b");
        return (int) pattern.matcher(content).results().count();
    }

    private int countMethods(String content) {
        Pattern pattern = Pattern.compile("\\)\\s*\\{");
        return (int) pattern.matcher(content).results().count();
    }

    private int countFields(String content) {
        Pattern pattern = Pattern.compile("\\w+\\s+\\w+\\s*[=;]");
        return (int) pattern.matcher(content).results().count();
    }

    private double calculateAverageCyclomaticComplexity(String content) {
        String[] methods = content.split("public|private|protected");
        if (methods.length == 0) return 1.0;
        
        double totalComplexity = 0;
        for (String method : methods) {
            totalComplexity += javaCodeParser.calculateCyclomaticComplexity(method);
        }
        
        return totalComplexity / methods.length;
    }

    private double calculateMaintainabilityIndex(CodeMetrics metrics) {
        // Maintainability Index formula
        // MI = 171 - 5.2 * ln(Halstead Volume) - 0.23 * Cyclomatic Complexity - 16.2 * ln(Lines of Code)
        double volume = Math.max(metrics.getCodeLines(), 1);
        double complexity = metrics.getCyclomaticComplexity();
        double loc = Math.max(metrics.getCodeLines(), 1);

        double mi = 171 - (5.2 * Math.log(volume)) - (0.23 * complexity) - (16.2 * Math.log(loc));
        return Math.max(0, Math.min(100, mi)); // Clamp between 0 and 100
    }
}
