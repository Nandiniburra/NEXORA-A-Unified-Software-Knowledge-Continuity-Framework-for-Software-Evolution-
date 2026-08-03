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

@Node("CodeMethod")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeMethodNode {
    @Id
    private String id;

    @Property
    private String methodName;

    @Property
    private String returnType;

    @Property
    private String parameters;

    @Property
    private String visibility;

    @Property
    private int cyclomaticComplexity;

    @Property
    private int lineCount;

    @Property
    private boolean isStatic;

    @Property
    private boolean isAbstract;

    @Relationship(type = "CALLS", direction = Relationship.Direction.OUTGOING)
    private List<CodeMethodNode> calledMethods;

    @Relationship(type = "USES_CLASS", direction = Relationship.Direction.OUTGOING)
    private List<CodeClassNode> usedClasses;
}
