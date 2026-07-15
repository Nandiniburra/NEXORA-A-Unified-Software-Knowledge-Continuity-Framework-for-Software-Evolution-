package com.nexora.repository;

import com.nexora.entity.CodeAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeAnalysisRepository extends JpaRepository<CodeAnalysis, Long> {
    List<CodeAnalysis> findByRepositoryId(Long repositoryId);
    Optional<CodeAnalysis> findLatestByRepositoryId(Long repositoryId);
}
