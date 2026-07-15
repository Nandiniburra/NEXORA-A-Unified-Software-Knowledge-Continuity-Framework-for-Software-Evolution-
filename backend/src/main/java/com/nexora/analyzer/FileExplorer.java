package com.nexora.analyzer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FileExplorer {

    public Map<String, Integer> exploreDirectory(String directoryPath) throws IOException {
        Map<String, Integer> fileTypeCount = new HashMap<>();
        
        Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    String extension = getFileExtension(path.getFileName().toString());
                    fileTypeCount.merge(extension, 1, Integer::sum);
                });
        
        return fileTypeCount;
    }

    public List<String> findFilesByExtension(String directoryPath, String extension) throws IOException {
        return Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)
                .filter(p -> p.getFileName().toString().endsWith("." + extension))
                .map(Path::toString)
                .collect(Collectors.toList());
    }

    public Map<String, Object> getDirectoryStructure(String directoryPath) throws IOException {
        Path root = Paths.get(directoryPath);
        Map<String, Object> structure = new HashMap<>();
        
        structure.put("name", root.getFileName().toString());
        structure.put("type", "directory");
        structure.put("path", root.toString());
        
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalFiles", (int) Files.walk(root).filter(Files::isRegularFile).count());
        stats.put("totalDirectories", (int) Files.walk(root).filter(Files::isDirectory).count());
        
        structure.put("stats", stats);
        structure.put("fileTypes", exploreDirectory(directoryPath));
        
        return structure;
    }

    public List<String> listJavaFiles(String directoryPath) throws IOException {
        return findFilesByExtension(directoryPath, "java");
    }

    public List<String> listConfigFiles(String directoryPath) throws IOException {
        List<String> configFiles = new ArrayList<>();
        Path root = Paths.get(directoryPath);
        
        // Common config file patterns
        String[] patterns = {"pom.xml", "build.gradle", "settings.gradle", "application.properties", 
                           "application.yml", "docker-compose.yml", ".env", "Dockerfile"};
        
        Files.walk(root)
                .filter(Files::isRegularFile)
                .forEach(path -> {
                    String fileName = path.getFileName().toString();
                    for (String pattern : patterns) {
                        if (fileName.equals(pattern) || fileName.endsWith(pattern)) {
                            configFiles.add(path.toString());
                        }
                    }
                });
        
        return configFiles;
    }

    public long getDirectorySize(String directoryPath) throws IOException {
        return Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)
                .mapToLong(this::getFileSize)
                .sum();
    }

    private long getFileSize(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            log.warn("Failed to get size of file: {}", path, e);
            return 0;
        }
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return (lastDot > 0) ? fileName.substring(lastDot + 1) : "unknown";
    }
}
