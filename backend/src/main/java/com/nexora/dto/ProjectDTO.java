package com.nexora.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {
    private Long id;
    private String name;
    private String description;
    private String repositoryUrl;
    private String framework;
    private String language;
    private String architecturePattern;
    private Boolean isActive;
}
