package com.nexora.parser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassInfo {
    private String className;
    private String packageName;
    private String filePath;
    private String visibility; // public, private, protected
    private boolean isAbstract;
    private boolean isInterface;
    private boolean isEnum;
    private List<String> superClasses;
    private List<String> interfaces;
    private List<MethodInfo> methods;
    private List<FieldInfo> fields;
    private int lineCount;
    private String documentation;
}
