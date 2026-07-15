package com.nexora.parser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FieldInfo {
    private String fieldName;
    private String fieldType;
    private String visibility; // public, private, protected
    private boolean isStatic;
    private boolean isFinal;
    private String documentation;
}
