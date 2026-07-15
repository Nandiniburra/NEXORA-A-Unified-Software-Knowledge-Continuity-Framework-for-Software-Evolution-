package com.nexora.controller;

import com.nexora.dto.CodeAnalysisResultDTO;
import com.nexora.service.RepositoryAnalyzerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/repositories/analyze")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class RepositoryAnalyzerController {

    @Autowired
    private RepositoryAnalyzerService repositoryAnalyzerService;

    @PostMapping("/from-url")
    public ResponseEntity<CodeAnalysisResultDTO> analyzeFromUrl(
            @RequestParam String repositoryUrl,
            @RequestParam String repositoryName,
            @RequestParam Long repositoryId) {
        
        log.info("Analyzing repository from URL: {}", repositoryUrl);
        CodeAnalysisResultDTO result = repositoryAnalyzerService.analyzeRepositoryFromUrl(
                repositoryUrl,
                repositoryName,
                repositoryId
        );
        
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/from-path")
    public ResponseEntity<CodeAnalysisResultDTO> analyzeFromPath(
            @RequestParam String repositoryPath,
            @RequestParam String repositoryName,
            @RequestParam Long repositoryId) {
        
        log.info("Analyzing repository from path: {}", repositoryPath);
        CodeAnalysisResultDTO result = repositoryAnalyzerService.analyzeRepository(
                repositoryPath,
                repositoryName,
                repositoryId
        );
        
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping("/async")
    public ResponseEntity<Map<String, Object>> analyzeAsync(
            @RequestParam String repositoryUrl,
            @RequestParam String repositoryName,
            @RequestParam Long repositoryId) {
        
        log.info("Starting async analysis: {}", repositoryName);
        
        // TODO: Implement async processing with job queue
        Map<String, Object> response = new HashMap<>();
        response.put("status", "QUEUED");
        response.put("jobId", UUID.randomUUID().toString());
        response.put("repositoryName", repositoryName);
        response.put("message", "Analysis queued for processing");
        
        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/status/{jobId}")
    public ResponseEntity<Map<String, Object>> getAnalysisStatus(@PathVariable String jobId) {
        log.info("Getting status for job: {}", jobId);
        
        // TODO: Fetch job status from database
        Map<String, Object> response = new HashMap<>();
        response.put("jobId", jobId);
        response.put("status", "IN_PROGRESS");
        response.put("progress", 45);
        
        return ResponseEntity.ok(response);
    }
}
