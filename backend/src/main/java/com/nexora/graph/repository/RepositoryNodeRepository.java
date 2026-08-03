package com.nexora.graph.repository;

import com.nexora.graph.model.RepositoryNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RepositoryNodeRepository extends Neo4jRepository<RepositoryNode, String> {
    Optional<RepositoryNode> findByRepositoryName(String repositoryName);
}
