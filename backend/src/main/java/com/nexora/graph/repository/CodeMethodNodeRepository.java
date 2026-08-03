package com.nexora.graph.repository;

import com.nexora.graph.model.CodeMethodNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CodeMethodNodeRepository extends Neo4jRepository<CodeMethodNode, String> {
    
    @Query("MATCH (m:CodeMethod)-[:CALLS]->(called:CodeMethod) WHERE m.id = $methodId RETURN called")
    List<CodeMethodNode> findCalledMethods(String methodId);
    
    @Query("MATCH (m:CodeMethod)<-[:CALLS]-(caller:CodeMethod) WHERE m.id = $methodId RETURN caller")
    List<CodeMethodNode> findCallingMethods(String methodId);
    
    @Query("MATCH (m:CodeMethod) WHERE m.cyclomaticComplexity > $threshold RETURN m ORDER BY m.cyclomaticComplexity DESC")
    List<CodeMethodNode> findComplexMethods(int threshold);
}
