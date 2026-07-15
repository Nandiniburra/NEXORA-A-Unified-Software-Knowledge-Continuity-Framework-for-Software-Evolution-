package com.nexora.analyzer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class DependencyExtractor {

    public List<DependencyInfo> extractMavenDependencies(String pomFilePath) {
        List<DependencyInfo> dependencies = new ArrayList<>();
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(pomFilePath));
            
            NodeList dependencyNodes = doc.getElementsByTagName("dependency");
            
            for (int i = 0; i < dependencyNodes.getLength(); i++) {
                Element depElement = (Element) dependencyNodes.item(i);
                
                String groupId = depElement.getElementsByTagName("groupId").item(0).getTextContent();
                String artifactId = depElement.getElementsByTagName("artifactId").item(0).getTextContent();
                String version = depElement.getElementsByTagName("version").item(0) != null 
                        ? depElement.getElementsByTagName("version").item(0).getTextContent() 
                        : "unknown";
                String scope = depElement.getElementsByTagName("scope").item(0) != null 
                        ? depElement.getElementsByTagName("scope").item(0).getTextContent() 
                        : "compile";
                
                DependencyInfo dep = DependencyInfo.builder()
                        .dependencyName(groupId + ":" + artifactId)
                        .version(version)
                        .type(scope.toUpperCase())
                        .source("MAVEN")
                        .build();
                
                dependencies.add(dep);
            }
            
            log.info("Extracted {} Maven dependencies from {}", dependencies.size(), pomFilePath);
        } catch (Exception e) {
            log.error("Failed to parse pom.xml: {}", pomFilePath, e);
        }
        
        return dependencies;
    }

    public List<DependencyInfo> extractGradleDependencies(String buildGradlePath) {
        List<DependencyInfo> dependencies = new ArrayList<>();
        
        try {
            String content = Files.readString(Paths.get(buildGradlePath));
            Pattern pattern = Pattern.compile("(?:implementation|testImplementation|compile|testCompile)\\s+['\"]([^'\"]+)['\"]\\s*");
            Matcher matcher = pattern.matcher(content);
            
            while (matcher.find()) {
                String depString = matcher.group(1);
                String[] parts = depString.split(":");
                
                if (parts.length >= 2) {
                    String type = matcher.group(0).contains("test") ? "TEST" : "COMPILE";
                    
                    DependencyInfo dep = DependencyInfo.builder()
                            .dependencyName(parts[0] + ":" + parts[1])
                            .version(parts.length > 2 ? parts[2] : "unknown")
                            .type(type)
                            .source("GRADLE")
                            .build();
                    
                    dependencies.add(dep);
                }
            }
            
            log.info("Extracted {} Gradle dependencies from {}", dependencies.size(), buildGradlePath);
        } catch (IOException e) {
            log.error("Failed to parse build.gradle: {}", buildGradlePath, e);
        }
        
        return dependencies;
    }

    public List<DependencyInfo> extractNpmDependencies(String packageJsonPath) {
        List<DependencyInfo> dependencies = new ArrayList<>();
        
        try {
            String content = Files.readString(Paths.get(packageJsonPath));
            
            // Parse dependencies section
            Pattern depPattern = Pattern.compile("\\"([^\"]+)\\"\\s*:\\s*\\"([^\"]+)\"");
            Matcher depMatcher = depPattern.matcher(content);
            
            while (depMatcher.find()) {
                DependencyInfo dep = DependencyInfo.builder()
                        .dependencyName(depMatcher.group(1))
                        .version(depMatcher.group(2))
                        .type("RUNTIME")
                        .source("NPM")
                        .build();
                
                dependencies.add(dep);
            }
            
            log.info("Extracted {} NPM dependencies from {}", dependencies.size(), packageJsonPath);
        } catch (IOException e) {
            log.error("Failed to parse package.json: {}", packageJsonPath, e);
        }
        
        return dependencies;
    }

    public List<DependencyInfo> extractAllDependencies(String repositoryPath) {
        List<DependencyInfo> allDependencies = new ArrayList<>();
        
        try {
            // Check for pom.xml
            Path pomPath = Paths.get(repositoryPath, "pom.xml");
            if (Files.exists(pomPath)) {
                allDependencies.addAll(extractMavenDependencies(pomPath.toString()));
            }
            
            // Check for build.gradle
            Path gradlePath = Paths.get(repositoryPath, "build.gradle");
            if (Files.exists(gradlePath)) {
                allDependencies.addAll(extractGradleDependencies(gradlePath.toString()));
            }
            
            // Check for package.json
            Path npmPath = Paths.get(repositoryPath, "package.json");
            if (Files.exists(npmPath)) {
                allDependencies.addAll(extractNpmDependencies(npmPath.toString()));
            }
            
        } catch (Exception e) {
            log.error("Error extracting dependencies from repository: {}", repositoryPath, e);
        }
        
        return allDependencies;
    }
}
