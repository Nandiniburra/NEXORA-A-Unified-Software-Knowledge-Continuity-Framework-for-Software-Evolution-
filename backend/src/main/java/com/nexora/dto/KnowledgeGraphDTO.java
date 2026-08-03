package com.nexora.dto;

import com.nexora.graph.model.CodeClassNode;
import com.nexora.graph.model.CodeMethodNode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KnowledgeGraphDTO {
    private String repositoryName;
    private int totalNodes;
    private int totalEdges;
    private List<CodeClassNode> classes;
    private List<CodeMethodNode> methods;
    private double averageComplexity;
    private int criticalMethods; // Methods with high complexity
}
