package com.nexora.graph.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Node("CodeClass")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeClassNode {
    @Id
    private String id;

    @Property
    private String className;

    @Property
    private String packageName;

    @Property
    private String filePath;

    @Property
    private int lineCount;

    @Property
    private boolean isAbstract;

    @Property
    private boolean isInterface;

    @Property
    private String visibility;

    @Property
    private double complexity;

    @Relationship(type = "EXTENDS", direction = Relationship.Direction.OUTGOING)
    private CodeClassNode superClass;

    @Relationship(type = "IMPLEMENTS", direction = Relationship.Direction.OUTGOING)
    private List<CodeInterfaceNode> interfaces;

    @Relationship(type = "CONTAINS", direction = Relationship.Direction.OUTGOING)
    private List<CodeMethodNode> methods;

    @Relationship(type = "HAS_FIELD", direction = Relationship.Direction.OUTGOING)
    private List<CodeFieldNode> fields;

    @Relationship(type = "DEPENDS_ON", direction = Relationship.Direction.OUTGOING)
    private List<CodeClassNode> dependencies;

    public CodeClassNode(String className, String packageName) {
        this.id = packageName + "." + className;
        this.className = className;
        this.packageName = packageName;
        this.methods = new ArrayList<>();
        this.fields = new ArrayList<>();
        this.dependencies = new ArrayList<>();
    }
}
