package com.nexora.graph.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Node("CodeField")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeFieldNode {
    @Id
    private String id;

    @Property
    private String fieldName;

    @Property
    private String fieldType;

    @Property
    private String visibility;

    @Property
    private boolean isStatic;

    @Property
    private boolean isFinal;
}
