package com.nexora.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@Configuration
@EnableNeo4jRepositories(basePackages = "com.nexora.graph.repository")
public class Neo4jConfig {

    @Bean
    public String neo4jConfig() {
        return "Neo4j configuration loaded";
    }
}
