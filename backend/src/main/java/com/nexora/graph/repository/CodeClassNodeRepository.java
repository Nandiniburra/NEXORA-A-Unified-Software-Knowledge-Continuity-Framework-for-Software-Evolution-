package com.nexora.graph.repository;

import com.nexora.graph.model.CodeClassNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CodeClassNodeRepository extends Neo4jRepository<CodeClassNode, String> {
    Optional<CodeClassNode> findByClassName(String className);
    List<CodeClassNode> findByPackageName(String packageName);
    
    @Query("MATCH (c:CodeClass) WHERE c.packageName CONTAINS $packageName RETURN c")
    List<CodeClassNode> findClassesByPackagePattern(String packageName);
    
    @Query("MATCH (c:CodeClass)-[:DEPENDS_ON]->(d:CodeClass) WHERE c.id = $classId RETURN d")
    List<CodeClassNode> findDependencies(String classId);
    
    @Query("MATCH (c:CodeClass)<-[:DEPENDS_ON]-(dependent:CodeClass) WHERE c.id = $classId RETURN dependent")
    List<CodeClassNode> findDependents(String classId);
}
