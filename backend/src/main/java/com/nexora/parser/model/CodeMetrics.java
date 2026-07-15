package com.nexora.parser.model;

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
public class CodeMetrics {
    private String fileName;
    private String filePath;
    private int totalLines;
    private int codeLines;
    private int commentLines;
    private int blankLines;
    private int classCount;
    private int methodCount;
    private int fieldCount;
    private double cyclomaticComplexity;
    private double maintainabilityIndex;
    private List<String> dependencies;
    private Map<String, Integer> complexityPerMethod;
    private double commentRatio;
}
