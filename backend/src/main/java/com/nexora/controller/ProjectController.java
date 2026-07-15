package com.nexora.controller;

import com.nexora.dto.CreateProjectRequest;
import com.nexora.dto.ProjectDTO;
import com.nexora.service.ProjectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody CreateProjectRequest request) {
        Long userId = extractUserIdFromAuth();
        ProjectDTO projectDTO = projectService.createProject(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectDTO);
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getUserProjects() {
        Long userId = extractUserIdFromAuth();
        List<ProjectDTO> projects = projectService.getUserProjects(userId);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        Long userId = extractUserIdFromAuth();
        ProjectDTO projectDTO = projectService.getProjectById(id, userId);
        return ResponseEntity.ok(projectDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody CreateProjectRequest request) {
        Long userId = extractUserIdFromAuth();
        ProjectDTO projectDTO = projectService.updateProject(id, request, userId);
        return ResponseEntity.ok(projectDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        Long userId = extractUserIdFromAuth();
        projectService.deleteProject(id, userId);
        return ResponseEntity.ok("Project deleted successfully");
    }

    private Long extractUserIdFromAuth() {
        // TODO: Implement proper user extraction from JWT token
        // For now, returning dummy ID - will be updated later
        return 1L;
    }
}
