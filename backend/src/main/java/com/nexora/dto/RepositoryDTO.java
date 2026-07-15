package com.nexora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepositoryDTO {
    private Long id;
    private String name;
    private String url;
    private String branch;
    private Integer commitCount;
    private Integer contributorCount;
    private LocalDateTime lastAnalyzed;
    private Boolean isActive;
}
