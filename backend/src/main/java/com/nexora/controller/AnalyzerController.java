package com.nexora.controller;

import com.nexora.dto.CodeAnalysisResultDTO;
import com.nexora.service.CodeAnalyzerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/analyze")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class AnalyzerController {

    @Autowired
    private CodeAnalyzerService codeAnalyzerService;

    @PostMapping("/repository")
    public ResponseEntity<CodeAnalysisResultDTO> analyzeRepository(
            @RequestParam String repositoryPath,
            @RequestParam String repositoryName,
            @RequestParam Long repositoryId) {
        
        log.info("Starting analysis for repository: {}", repositoryName);
        CodeAnalysisResultDTO result = codeAnalyzerService.analyzeRepository(
                repositoryPath, 
                repositoryName, 
                repositoryId
        );
        
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/repository/{repositoryId}")
    public ResponseEntity<Map<String, Object>> getAnalysis(
            @PathVariable Long repositoryId) {
        
        Map<String, Object> analysis = codeAnalyzerService.getDetailedAnalysis(repositoryId);
        return ResponseEntity.ok(analysis);
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, String>> validateRepository(
            @RequestParam String repositoryPath) {
        
        Map<String, String> response = new HashMap<>();
        try {
            // Validate repository path exists and contains Java files
            java.nio.file.Path path = java.nio.file.Paths.get(repositoryPath);
            if (java.nio.file.Files.exists(path)) {
                long javaFiles = java.nio.file.Files.walk(path)
                        .filter(p -> p.toString().endsWith(".java"))
                        .count();
                
                if (javaFiles > 0) {
                    response.put("status", "valid");
                    response.put("javaFiles", String.valueOf(javaFiles));
                } else {
                    response.put("status", "invalid");
                    response.put("message", "No Java files found in repository");
                }
            } else {
                response.put("status", "invalid");
                response.put("message", "Repository path does not exist");
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
}
