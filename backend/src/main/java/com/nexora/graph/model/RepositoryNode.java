package com.nexora.graph.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Node("Repository")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepositoryNode {
    @Id
    private String id;

    @Property
    private String repositoryName;

    @Property
    private String repositoryUrl;

    @Property
    private String primaryLanguage;

    @Property
    private int totalClasses;

    @Property
    private int totalMethods;

    @Property
    private double averageComplexity;

    @Relationship(type = "CONTAINS_CLASS", direction = Relationship.Direction.OUTGOING)
    private List<CodeClassNode> classes;

    @Relationship(type = "USES_LIBRARY", direction = Relationship.Direction.OUTGOING)
    private List<LibraryNode> libraries;
}
