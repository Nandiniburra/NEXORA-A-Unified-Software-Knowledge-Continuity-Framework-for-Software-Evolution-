package com.nexora.controller;

import com.nexora.dto.DashboardMetricsDTO;
import com.nexora.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects/{projectId}/dashboard")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/metrics")
    public ResponseEntity<DashboardMetricsDTO> getDashboardMetrics(@PathVariable Long projectId) {
        Long userId = extractUserIdFromAuth();
        DashboardMetricsDTO metrics = dashboardService.getDashboardMetrics(projectId, userId);
        return ResponseEntity.ok(metrics);
    }

    @PostMapping("/metrics")
    public ResponseEntity<DashboardMetricsDTO> updateMetrics(
            @PathVariable Long projectId,
            @RequestBody DashboardMetricsDTO metricsDTO) {
        Long userId = extractUserIdFromAuth();
        DashboardMetricsDTO updated = dashboardService.updateMetrics(projectId, metricsDTO, userId);
        return ResponseEntity.ok(updated);
    }

    private Long extractUserIdFromAuth() {
        // TODO: Implement proper user extraction from JWT token
        // For now, returning dummy ID - will be updated later
        return 1L;
    }
}
