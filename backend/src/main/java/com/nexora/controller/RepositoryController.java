package com.nexora.controller;

import com.nexora.dto.RepositoryDTO;
import com.nexora.service.RepositoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/projects/{projectId}/repositories")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class RepositoryController {

    @Autowired
    private RepositoryService repositoryService;

    @PostMapping
    public ResponseEntity<RepositoryDTO> addRepository(
            @PathVariable Long projectId,
            @RequestParam String name,
            @RequestParam String url,
            @RequestParam(required = false) String branch) {
        Long userId = extractUserIdFromAuth();
        RepositoryDTO repositoryDTO = repositoryService.addRepository(projectId, name, url, branch, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(repositoryDTO);
    }

    @GetMapping
    public ResponseEntity<List<RepositoryDTO>> getProjectRepositories(@PathVariable Long projectId) {
        Long userId = extractUserIdFromAuth();
        List<RepositoryDTO> repositories = repositoryService.getProjectRepositories(projectId, userId);
        return ResponseEntity.ok(repositories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RepositoryDTO> getRepositoryById(
            @PathVariable Long projectId,
            @PathVariable Long id) {
        Long userId = extractUserIdFromAuth();
        RepositoryDTO repositoryDTO = repositoryService.getRepositoryById(id, projectId, userId);
        return ResponseEntity.ok(repositoryDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RepositoryDTO> updateRepository(
            @PathVariable Long projectId,
            @PathVariable Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String branch) {
        Long userId = extractUserIdFromAuth();
        RepositoryDTO repositoryDTO = repositoryService.updateRepository(id, projectId, name, branch, userId);
        return ResponseEntity.ok(repositoryDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteRepository(
            @PathVariable Long projectId,
            @PathVariable Long id) {
        Long userId = extractUserIdFromAuth();
        repositoryService.deleteRepository(id, projectId, userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Repository deleted successfully");
        return ResponseEntity.ok(response);
    }

    private Long extractUserIdFromAuth() {
        // TODO: Implement proper user extraction from JWT token
        // For now, returning dummy ID - will be updated later
        return 1L;
    }
}
