package com.nexora.repository;

import com.nexora.entity.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RepositoryRepository extends JpaRepository<Repository, Long> {
    List<Repository> findByProjectId(Long projectId);
    Optional<Repository> findByIdAndProjectId(Long id, Long projectId);
    List<Repository> findByProjectIdAndIsActiveTrue(Long projectId);
    Boolean existsByUrlAndProjectId(String url, Long projectId);
}
