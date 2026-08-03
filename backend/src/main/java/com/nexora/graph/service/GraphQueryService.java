package com.nexora.graph.service;

import com.nexora.graph.model.CodeClassNode;
import com.nexora.graph.model.CodeMethodNode;
import com.nexora.graph.repository.CodeClassNodeRepository;
import com.nexora.graph.repository.CodeMethodNodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class GraphQueryService {

    @Autowired
    private CodeClassNodeRepository classNodeRepository;

    @Autowired
    private CodeMethodNodeRepository methodNodeRepository;

    public List<CodeClassNode> findDependencyChain(String startClassName) {
        log.info("Finding dependency chain starting from: {}", startClassName);
        
        Optional<CodeClassNode> startClass = classNodeRepository.findByClassName(startClassName);
        if (startClass.isEmpty()) {
            return List.of();
        }

        return classNodeRepository.findDependencies(startClass.get().getId());
    }

    public List<CodeClassNode> findDependents(String className) {
        log.info("Finding classes that depend on: {}", className);
        
        Optional<CodeClassNode> classNode = classNodeRepository.findByClassName(className);
        if (classNode.isEmpty()) {
            return List.of();
        }

        return classNodeRepository.findDependents(classNode.get().getId());
    }

    public List<CodeMethodNode> findMethodCallPath(String startMethodId, String endMethodId) {
        log.info("Finding call path from {} to {}", startMethodId, endMethodId);
        
        // TODO: Implement BFS/DFS to find method call path
        return List.of();
    }

    public List<CodeClassNode> findImpactAnalysis(String changedClassName) {
        log.info("Analyzing impact of changes to: {}", changedClassName);
        return findDependents(changedClassName);
    }

    public int getComplexityScore(String className) {
        log.info("Calculating complexity score for: {}", className);
        
        Optional<CodeClassNode> classNode = classNodeRepository.findByClassName(className);
        if (classNode.isEmpty()) {
            return 0;
        }

        CodeClassNode node = classNode.get();
        return (int) node.getMethods().stream()
                .mapToInt(CodeMethodNode::getCyclomaticComplexity)
                .average()
                .orElse(0);
    }
}
