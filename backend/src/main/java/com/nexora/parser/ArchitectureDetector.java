package com.nexora.parser;

import com.nexora.parser.model.RepositoryMetrics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Pattern;

@Component
@Slf4j
public class ArchitectureDetector {

    private static final Map<String, String> PATTERN_SIGNATURES = new HashMap<>();

    static {
        PATTERN_SIGNATURES.put("MVC", "@Controller|@Service|@Repository|@RestController");
        PATTERN_SIGNATURES.put("LAYERED", "(controller|service|repository|entity|dto)");
        PATTERN_SIGNATURES.put("MICROSERVICES", "@SpringBootApplication|@EnableEureka|@FeignClient");
        PATTERN_SIGNATURES.put("EVENT_DRIVEN", "@Async|@EventListener|@Transactional|EventBus");
        PATTERN_SIGNATURES.put("PIPE_FILTER", "(filter|interceptor|middleware)");
        PATTERN_SIGNATURES.put("PLUGIN", "(PluginManager|PluginLoader|PluginInterface)");
    }

    public String detectArchitecturePattern(Path repositoryPath) throws IOException {
        StringBuilder allContent = new StringBuilder();
        List<Path> javaFiles = findJavaFiles(repositoryPath);
        
        for (Path file : javaFiles) {
            allContent.append(Files.readString(file)).append("\n");
        }

        return identifyPattern(allContent.toString());
    }

    private String identifyPattern(String content) {
        Map<String, Integer> patternScores = new HashMap<>();

        for (Map.Entry<String, String> entry : PATTERN_SIGNATURES.entrySet()) {
            Pattern pattern = Pattern.compile(entry.getValue(), Pattern.CASE_INSENSITIVE);
            int matches = (int) pattern.matcher(content).results().count();
            patternScores.put(entry.getKey(), matches);
        }

        return patternScores.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse("UNKNOWN");
    }

    public boolean detectDesignPatterns(String content) {
        // Singleton
        boolean hasSingleton = Pattern.compile("private\\s+static\\s+.*?instance|getInstance").matcher(content).find();
        
        // Factory
        boolean hasFactory = Pattern.compile("(Factory|Creator|Builder)").matcher(content).find();
        
        // Observer
        boolean hasObserver = Pattern.compile("(Observer|Listener|Event)").matcher(content).find();
        
        // Strategy
        boolean hasStrategy = Pattern.compile("(Strategy|Algorithm)").matcher(content).find();
        
        return hasSingleton || hasFactory || hasObserver || hasStrategy;
    }

    public int analyzeLayerComplexity(Path repositoryPath) throws IOException {
        Map<String, Integer> layerFiles = new HashMap<>();
        List<Path> javaFiles = findJavaFiles(repositoryPath);

        for (Path file : javaFiles) {
            String path = file.toString();
            if (path.contains("controller")) layerFiles.merge("controller", 1, Integer::sum);
            if (path.contains("service")) layerFiles.merge("service", 1, Integer::sum);
            if (path.contains("repository")) layerFiles.merge("repository", 1, Integer::sum);
            if (path.contains("entity")) layerFiles.merge("entity", 1, Integer::sum);
            if (path.contains("dto")) layerFiles.merge("dto", 1, Integer::sum);
        }

        return layerFiles.values().stream().mapToInt(Integer::intValue).sum();
    }

    private List<Path> findJavaFiles(Path dir) throws IOException {
        return Files.walk(dir)
                .filter(p -> p.toString().endsWith(".java"))
                .toList();
    }
}
