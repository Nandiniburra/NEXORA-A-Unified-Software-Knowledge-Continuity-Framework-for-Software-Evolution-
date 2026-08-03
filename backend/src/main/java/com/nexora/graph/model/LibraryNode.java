package com.nexora.graph.model;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Node("Library")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LibraryNode {
    @Id
    private String id;

    @Property
    private String libraryName;

    @Property
    private String version;

    @Property
    private String type; // EXTERNAL, INTERNAL

    @Property
    private String description;
}
