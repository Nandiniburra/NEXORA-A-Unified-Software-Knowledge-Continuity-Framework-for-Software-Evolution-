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
public class MethodInfo {
    private String methodName;
    private String returnType;
    private List<String> parameters;
    private String visibility; // public, private, protected
    private boolean isAbstract;
    private boolean isStatic;
    private boolean isFinal;
    private int cyclomaticComplexity;
    private int lineCount;
    private String documentation;
}
