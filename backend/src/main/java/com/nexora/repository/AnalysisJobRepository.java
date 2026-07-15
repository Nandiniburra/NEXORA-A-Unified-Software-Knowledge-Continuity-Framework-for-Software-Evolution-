package com.nexora.repository;

import com.nexora.entity.AnalysisJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnalysisJobRepository extends JpaRepository<AnalysisJob, Long> {
    Optional<AnalysisJob> findByJobId(String jobId);
    List<AnalysisJob> findByRepositoryId(Long repositoryId);
    List<AnalysisJob> findByStatus(String status);
}
