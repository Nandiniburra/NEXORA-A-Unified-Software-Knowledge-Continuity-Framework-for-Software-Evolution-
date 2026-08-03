package com.nexora.graph.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Node("CodeInterface")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeInterfaceNode {
    @Id
    private String id;

    @Property
    private String interfaceName;

    @Property
    private String packageName;

    @Property
    private String filePath;
}
