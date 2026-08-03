package com.nexora.graph.service;

import com.nexora.graph.model.*;
import com.nexora.graph.repository.CodeClassNodeRepository;
import com.nexora.graph.repository.CodeMethodNodeRepository;
import com.nexora.graph.repository.RepositoryNodeRepository;
import com.nexora.parser.model.ClassInfo;
import com.nexora.parser.model.FieldInfo;
import com.nexora.parser.model.MethodInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KnowledgeGraphService {

    @Autowired
    private CodeClassNodeRepository classNodeRepository;

    @Autowired
    private CodeMethodNodeRepository methodNodeRepository;

    @Autowired
    private RepositoryNodeRepository repositoryNodeRepository;

    @Autowired
    private GraphRelationshipBuilder relationshipBuilder;

    @Transactional
    public void createRepositoryGraph(String repositoryName, String repositoryUrl, 
                                     List<ClassInfo> classes, String primaryLanguage) {
        log.info("Creating knowledge graph for repository: {}", repositoryName);

        // Create repository node
        RepositoryNode repoNode = RepositoryNode.builder()
                .id(repositoryName)
                .repositoryName(repositoryName)
                .repositoryUrl(repositoryUrl)
                .primaryLanguage(primaryLanguage)
                .totalClasses(classes.size())
                .totalMethods((int) classes.stream().mapToLong(c -> c.getMethods().size()).sum())
                .build();

        // Create class nodes and their relationships
        List<CodeClassNode> classNodes = classes.stream()
                .map(this::mapClassInfoToNode)
                .collect(Collectors.toList());

        // Save all nodes
        classNodes.forEach(classNodeRepository::save);
        
        repoNode.setClasses(classNodes);
        repositoryNodeRepository.save(repoNode);

        log.info("Knowledge graph created successfully for repository: {}", repositoryName);
    }

    @Transactional
    public void updateClassDependencies(List<ClassInfo> classes) {
        log.info("Updating class dependencies in knowledge graph");

        classes.forEach(classInfo -> {
            Optional<CodeClassNode> classNode = classNodeRepository.findByClassName(classInfo.getClassName());
            if (classNode.isPresent()) {
                // TODO: Extract dependencies from imports and build relationships
                classNodeRepository.save(classNode.get());
            }
        });
    }

    public Map<String, Object> getClassDependencies(String className) {
        log.info("Retrieving dependencies for class: {}", className);
        
        Optional<CodeClassNode> classNode = classNodeRepository.findByClassName(className);
        if (classNode.isEmpty()) {
            return Collections.emptyMap();
        }

        CodeClassNode node = classNode.get();
        Map<String, Object> result = new HashMap<>();
        result.put("className", className);
        result.put("dependencies", node.getDependencies());
        result.put("dependencyCount", node.getDependencies().size());

        return result;
    }

    public Map<String, Object> getMethodCallHierarchy(String methodId) {
        log.info("Retrieving method call hierarchy for: {}", methodId);
        
        Optional<CodeMethodNode> methodNode = methodNodeRepository.findById(methodId);
        if (methodNode.isEmpty()) {
            return Collections.emptyMap();
        }

        CodeMethodNode method = methodNode.get();
        Map<String, Object> result = new HashMap<>();
        result.put("methodId", methodId);
        result.put("methodName", method.getMethodName());
        result.put("calledMethods", methodNodeRepository.findCalledMethods(methodId));
        result.put("callingMethods", methodNodeRepository.findCallingMethods(methodId));

        return result;
    }

    public List<CodeMethodNode> findComplexMethods(int complexityThreshold) {
        log.info("Finding methods with complexity > {}", complexityThreshold);
        return methodNodeRepository.findComplexMethods(complexityThreshold);
    }

    public Map<String, Object> analyzeCodeQuality(String repositoryName) {
        log.info("Analyzing code quality for repository: {}", repositoryName);
        
        Optional<RepositoryNode> repoNode = repositoryNodeRepository.findByRepositoryName(repositoryName);
        if (repoNode.isEmpty()) {
            return Collections.emptyMap();
        }

        RepositoryNode repo = repoNode.get();
        Map<String, Object> qualityMetrics = new HashMap<>();
        
        qualityMetrics.put("repositoryName", repositoryName);
        qualityMetrics.put("totalClasses", repo.getTotalClasses());
        qualityMetrics.put("totalMethods", repo.getTotalMethods());
        qualityMetrics.put("averageComplexity", repo.getAverageComplexity());
        qualityMetrics.put("complexMethods", findComplexMethods(10));

        return qualityMetrics;
    }

    @Transactional
    public void clearRepositoryGraph(String repositoryName) {
        log.info("Clearing knowledge graph for repository: {}", repositoryName);
        
        Optional<RepositoryNode> repoNode = repositoryNodeRepository.findByRepositoryName(repositoryName);
        repoNode.ifPresent(repositoryNodeRepository::delete);
    }

    private CodeClassNode mapClassInfoToNode(ClassInfo classInfo) {
        CodeClassNode node = new CodeClassNode(classInfo.getClassName(), classInfo.getPackageName());
        node.setFilePath(classInfo.getFilePath());
        node.setLineCount(classInfo.getLineCount());
        node.setAbstract(classInfo.isAbstract());
        node.setInterface(classInfo.isInterface());
        node.setVisibility(classInfo.getVisibility());

        // Map methods
        if (classInfo.getMethods() != null) {
            node.setMethods(classInfo.getMethods().stream()
                    .map(this::mapMethodInfoToNode)
                    .collect(Collectors.toList()));
        }

        // Map fields
        if (classInfo.getFields() != null) {
            node.setFields(classInfo.getFields().stream()
                    .map(this::mapFieldInfoToNode)
                    .collect(Collectors.toList()));
        }

        return node;
    }

    private CodeMethodNode mapMethodInfoToNode(MethodInfo methodInfo) {
        CodeMethodNode node = new CodeMethodNode();
        node.setMethodName(methodInfo.getMethodName());
        node.setReturnType(methodInfo.getReturnType());
        node.setVisibility(methodInfo.getVisibility());
        node.setCyclomaticComplexity(methodInfo.getCyclomaticComplexity());
        node.setLineCount(methodInfo.getLineCount());
        node.setStatic(methodInfo.isStatic());
        node.setAbstract(methodInfo.isAbstract());
        return node;
    }

    private CodeFieldNode mapFieldInfoToNode(FieldInfo fieldInfo) {
        CodeFieldNode node = new CodeFieldNode();
        node.setFieldName(fieldInfo.getFieldName());
        node.setFieldType(fieldInfo.getFieldType());
        node.setVisibility(fieldInfo.getVisibility());
        node.setStatic(fieldInfo.isStatic());
        node.setFinal(fieldInfo.isFinal());
        return node;
    }
}
