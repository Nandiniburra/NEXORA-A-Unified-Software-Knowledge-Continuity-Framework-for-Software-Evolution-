package com.nexora.service;

import com.nexora.dto.RepositoryDTO;
import com.nexora.entity.Project;
import com.nexora.entity.Repository;
import com.nexora.repository.ProjectRepository;
import com.nexora.repository.RepositoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RepositoryService {

    @Autowired
    private RepositoryRepository repositoryRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Transactional
    public RepositoryDTO addRepository(Long projectId, String name, String url, String branch, Long userId) {
        Project project = projectRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (repositoryRepository.existsByUrlAndProjectId(url, projectId)) {
            throw new RuntimeException("Repository with this URL already exists in the project");
        }

        Repository repository = Repository.builder()
                .project(project)
                .name(name)
                .url(url)
                .branch(branch != null ? branch : "main")
                .isActive(true)
                .build();

        repositoryRepository.save(repository);
        log.info("Repository added: {} to project: {}", name, projectId);
        return convertToDTO(repository);
    }

    public List<RepositoryDTO> getProjectRepositories(Long projectId, Long userId) {
        Project project = projectRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        return repositoryRepository.findByProjectIdAndIsActiveTrue(projectId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RepositoryDTO getRepositoryById(Long id, Long projectId, Long userId) {
        Project project = projectRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Repository repository = repositoryRepository.findByIdAndProjectId(id, projectId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));

        return convertToDTO(repository);
    }

    @Transactional
    public RepositoryDTO updateRepository(Long id, Long projectId, String name, String branch, Long userId) {
        Project project = projectRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Repository repository = repositoryRepository.findByIdAndProjectId(id, projectId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));

        if (name != null) {
            repository.setName(name);
        }
        if (branch != null) {
            repository.setBranch(branch);
        }

        repositoryRepository.save(repository);
        log.info("Repository updated: {}", id);
        return convertToDTO(repository);
    }

    @Transactional
    public void deleteRepository(Long id, Long projectId, Long userId) {
        Project project = projectRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Repository repository = repositoryRepository.findByIdAndProjectId(id, projectId)
                .orElseThrow(() -> new RuntimeException("Repository not found"));

        repository.setIsActive(false);
        repositoryRepository.save(repository);
        log.info("Repository deleted: {}", id);
    }

    private RepositoryDTO convertToDTO(Repository repository) {
        return RepositoryDTO.builder()
                .id(repository.getId())
                .name(repository.getName())
                .url(repository.getUrl())
                .branch(repository.getBranch())
                .commitCount(repository.getCommitCount())
                .contributorCount(repository.getContributorCount())
                .lastAnalyzed(repository.getLastAnalyzed())
                .isActive(repository.getIsActive())
                .build();
    }
}
