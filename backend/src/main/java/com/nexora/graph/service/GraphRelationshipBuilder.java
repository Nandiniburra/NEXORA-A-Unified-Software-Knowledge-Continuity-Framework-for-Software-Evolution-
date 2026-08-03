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
import java.util.regex.Pattern;

@Service
@Slf4j
public class GraphRelationshipBuilder {

    @Autowired
    private CodeClassNodeRepository classNodeRepository;

    @Autowired
    private CodeMethodNodeRepository methodNodeRepository;

    public void buildClassDependencies(CodeClassNode classNode, List<String> imports) {
        log.info("Building dependencies for class: {}", classNode.getClassName());
        
        for (String importStatement : imports) {
            // Parse import statement to get class name
            String className = extractClassNameFromImport(importStatement);
            
            Optional<CodeClassNode> dependencyNode = classNodeRepository.findByClassName(className);
            if (dependencyNode.isPresent()) {
                classNode.getDependencies().add(dependencyNode.get());
                log.debug("Added dependency: {} -> {}", classNode.getClassName(), className);
            }
        }
        
        classNodeRepository.save(classNode);
    }

    public void buildMethodCalls(CodeMethodNode sourceMethod, List<String> methodCalls) {
        log.info("Building method calls for: {}", sourceMethod.getMethodName());
        
        for (String methodCall : methodCalls) {
            // TODO: Parse method call and find target method
            // This would require more sophisticated parsing
        }
        
        methodNodeRepository.save(sourceMethod);
    }

    public void buildInheritanceHierarchy(CodeClassNode childClass, String parentClassName) {
        log.info("Building inheritance: {} extends {}", childClass.getClassName(), parentClassName);
        
        Optional<CodeClassNode> parentClass = classNodeRepository.findByClassName(parentClassName);
        parentClass.ifPresent(parent -> {
            childClass.setSuperClass(parent);
            classNodeRepository.save(childClass);
            log.debug("Inheritance relationship established");
        });
    }

    private String extractClassNameFromImport(String importStatement) {
        // Extract class name from import statement like "com.example.ClassName"
        String[] parts = importStatement.split("\\.");
        return parts.length > 0 ? parts[parts.length - 1] : importStatement;
    }
}
