package com.nexora.repository;

import com.nexora.entity.DashboardMetrics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DashboardMetricsRepository extends JpaRepository<DashboardMetrics, Long> {
    Optional<DashboardMetrics> findByProjectId(Long projectId);
}
