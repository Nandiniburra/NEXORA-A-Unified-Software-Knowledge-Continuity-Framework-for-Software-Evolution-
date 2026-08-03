package com.nexora.controller;

import com.nexora.graph.model.CodeClassNode;
import com.nexora.graph.model.CodeMethodNode;
import com.nexora.graph.service.KnowledgeGraphService;
import com.nexora.graph.service.GraphQueryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/knowledge-graph")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class KnowledgeGraphController {

    @Autowired
    private KnowledgeGraphService knowledgeGraphService;

    @Autowired
    private GraphQueryService graphQueryService;

    @GetMapping("/class/{className}/dependencies")
    public ResponseEntity<Map<String, Object>> getClassDependencies(
            @PathVariable String className) {
        
        log.info("Getting dependencies for class: {}", className);
        Map<String, Object> dependencies = knowledgeGraphService.getClassDependencies(className);
        
        return ResponseEntity.ok(dependencies);
    }

    @GetMapping("/class/{className}/dependents")
    public ResponseEntity<List<CodeClassNode>> getClassDependents(
            @PathVariable String className) {
        
        log.info("Getting dependents for class: {}", className);
        List<CodeClassNode> dependents = graphQueryService.findDependents(className);
        
        return ResponseEntity.ok(dependents);
    }

    @GetMapping("/method/{methodId}/hierarchy")
    public ResponseEntity<Map<String, Object>> getMethodCallHierarchy(
            @PathVariable String methodId) {
        
        log.info("Getting call hierarchy for method: {}", methodId);
        Map<String, Object> hierarchy = knowledgeGraphService.getMethodCallHierarchy(methodId);
        
        return ResponseEntity.ok(hierarchy);
    }

    @GetMapping("/methods/complex")
    public ResponseEntity<List<CodeMethodNode>> getComplexMethods(
            @RequestParam(defaultValue = "10") int threshold) {
        
        log.info("Finding methods with complexity > {}", threshold);
        List<CodeMethodNode> complexMethods = knowledgeGraphService.findComplexMethods(threshold);
        
        return ResponseEntity.ok(complexMethods);
    }

    @GetMapping("/repository/{repositoryName}/quality")
    public ResponseEntity<Map<String, Object>> analyzeCodeQuality(
            @PathVariable String repositoryName) {
        
        log.info("Analyzing code quality for repository: {}", repositoryName);
        Map<String, Object> qualityMetrics = knowledgeGraphService.analyzeCodeQuality(repositoryName);
        
        return ResponseEntity.ok(qualityMetrics);
    }

    @GetMapping("/class/{className}/impact")
    public ResponseEntity<List<CodeClassNode>> getImpactAnalysis(
            @PathVariable String className) {
        
        log.info("Performing impact analysis for class: {}", className);
        List<CodeClassNode> impactedClasses = graphQueryService.findImpactAnalysis(className);
        
        return ResponseEntity.ok(impactedClasses);
    }

    @GetMapping("/class/{className}/complexity")
    public ResponseEntity<Map<String, Integer>> getComplexityScore(
            @PathVariable String className) {
        
        log.info("Calculating complexity score for class: {}", className);
        int score = graphQueryService.getComplexityScore(className);
        
        return ResponseEntity.ok(Map.of("className", className, "complexityScore", score));
    }

    @GetMapping("/dependency-chain")
    public ResponseEntity<List<CodeClassNode>> getDependencyChain(
            @RequestParam String startClassName) {
        
        log.info("Finding dependency chain from: {}", startClassName);
        List<CodeClassNode> chain = graphQueryService.findDependencyChain(startClassName);
        
        return ResponseEntity.ok(chain);
    }
}
