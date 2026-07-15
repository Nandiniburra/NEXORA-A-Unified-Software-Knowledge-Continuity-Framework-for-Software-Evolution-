package com.nexora.service;

import com.nexora.dto.CreateProjectRequest;
import com.nexora.dto.ProjectDTO;
import com.nexora.entity.Project;
import com.nexora.entity.User;
import com.nexora.repository.ProjectRepository;
import com.nexora.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ProjectDTO createProject(CreateProjectRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (projectRepository.existsByNameAndUserId(request.getName(), userId)) {
            throw new RuntimeException("Project with this name already exists");
        }

        Project project = Project.builder()
                .user(user)
                .name(request.getName())
                .description(request.getDescription())
                .repositoryUrl(request.getRepositoryUrl())
                .isActive(true)
                .build();

        projectRepository.save(project);
        log.info("Project created: {} for user: {}", request.getName(), userId);
        return convertToDTO(project);
    }

    public List<ProjectDTO> getUserProjects(Long userId) {
        return projectRepository.findByUserIdAndIsActiveTrue(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProjectDTO getProjectById(Long id, Long userId) {
        Project project = projectRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return convertToDTO(project);
    }

    @Transactional
    public ProjectDTO updateProject(Long id, CreateProjectRequest request, Long userId) {
        Project project = projectRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setRepositoryUrl(request.getRepositoryUrl());

        projectRepository.save(project);
        log.info("Project updated: {}", id);
        return convertToDTO(project);
    }

    @Transactional
    public void deleteProject(Long id, Long userId) {
        Project project = projectRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        project.setIsActive(false);
        projectRepository.save(project);
        log.info("Project deleted: {}", id);
    }

    private ProjectDTO convertToDTO(Project project) {
        return ProjectDTO.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .repositoryUrl(project.getRepositoryUrl())
                .framework(project.getFramework())
                .language(project.getLanguage())
                .architecturePattern(project.getArchitecturePattern())
                .isActive(project.getIsActive())
                .build();
    }
}
