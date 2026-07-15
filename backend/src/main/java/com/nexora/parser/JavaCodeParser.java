package com.nexora.parser;

import com.nexora.parser.model.ClassInfo;
import com.nexora.parser.model.FieldInfo;
import com.nexora.parser.model.MethodInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JavaCodeParser {

    private static final Pattern CLASS_PATTERN = Pattern.compile(
            "(?:public|private|protected)?\\s*(?:abstract)?\\s*(?:final)?\\s*(?:class|interface|enum)\\s+(\\w+)(?:\\s*extends\\s+([\\w,\\s]+))?(?:\\s*implements\\s+([\\w,\\s]+))?"
    );

    private static final Pattern METHOD_PATTERN = Pattern.compile(
            "(?:public|private|protected)?\\s*(?:static)?\\s*(?:abstract)?\\s*(?:final)?\\s*(?:\\w+|<\\w+>)\\s+(\\w+)\\s*\\(([^)]*)\\)"
    );

    private static final Pattern FIELD_PATTERN = Pattern.compile(
            "(?:public|private|protected)?\\s*(?:static)?\\s*(?:final)?\\s*(\\w+)\\s+(\\w+)(?:\\s*=.*)?[;]"
    );

    private static final Pattern IMPORT_PATTERN = Pattern.compile(
            "^import\\s+(?:static\\s+)?([\\w.]+);$", Pattern.MULTILINE
    );

    public ClassInfo parseJavaFile(Path filePath) throws IOException {
        String content = Files.readString(filePath);
        return parseJavaContent(content, filePath.toString());
    }

    public ClassInfo parseJavaContent(String content, String filePath) {
        ClassInfo classInfo = ClassInfo.builder()
                .filePath(filePath)
                .lineCount(countLines(content))
                .methods(new ArrayList<>())
                .fields(new ArrayList<>())
                .superClasses(new ArrayList<>())
                .interfaces(new ArrayList<>())
                .build();

        // Extract package
        Pattern packagePattern = Pattern.compile("^package\\s+([\\w.]+);", Pattern.MULTILINE);
        Matcher packageMatcher = packagePattern.matcher(content);
        if (packageMatcher.find()) {
            classInfo.setPackageName(packageMatcher.group(1));
        }

        // Extract class information
        Matcher classMatcher = CLASS_PATTERN.matcher(content);
        if (classMatcher.find()) {
            classInfo.setClassName(classMatcher.group(1));
            
            if (classMatcher.group(2) != null) {
                classInfo.setSuperClasses(Arrays.stream(classMatcher.group(2).split(","))
                        .map(String::trim)
                        .collect(Collectors.toList()));
            }
            
            if (classMatcher.group(3) != null) {
                classInfo.setInterfaces(Arrays.stream(classMatcher.group(3).split(","))
                        .map(String::trim)
                        .collect(Collectors.toList()));
            }

            classInfo.setAbstract(content.contains("abstract class"));
            classInfo.setInterface(content.contains("interface") && !content.contains("interface"));
            classInfo.setEnum(content.contains("enum"));
        }

        // Extract methods
        Matcher methodMatcher = METHOD_PATTERN.matcher(content);
        while (methodMatcher.find()) {
            MethodInfo methodInfo = MethodInfo.builder()
                    .methodName(methodMatcher.group(1))
                    .parameters(Arrays.asList(methodMatcher.group(2).split(",")))
                    .cyclomaticComplexity(calculateCyclomaticComplexity(content))
                    .build();
            classInfo.getMethods().add(methodInfo);
        }

        // Extract fields
        Matcher fieldMatcher = FIELD_PATTERN.matcher(content);
        while (fieldMatcher.find()) {
            FieldInfo fieldInfo = FieldInfo.builder()
                    .fieldType(fieldMatcher.group(1))
                    .fieldName(fieldMatcher.group(2))
                    .build();
            classInfo.getFields().add(fieldInfo);
        }

        return classInfo;
    }

    public List<String> extractImports(String content) {
        List<String> imports = new ArrayList<>();
        Matcher importMatcher = IMPORT_PATTERN.matcher(content);
        while (importMatcher.find()) {
            imports.add(importMatcher.group(1));
        }
        return imports;
    }

    public int calculateCyclomaticComplexity(String methodContent) {
        int complexity = 1;
        complexity += countMatches(methodContent, "if\\s*\\(");
        complexity += countMatches(methodContent, "for\\s*\\(");
        complexity += countMatches(methodContent, "while\\s*\\(");
        complexity += countMatches(methodContent, "catch\\s*\\(");
        complexity += countMatches(methodContent, "case\\s+");
        complexity += countMatches(methodContent, "\\?\\s*:");
        return complexity;
    }

    private int countMatches(String content, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(content);
        int count = 0;
        while (m.find()) {
            count++;
        }
        return count;
    }

    private int countLines(String content) {
        return content.split("\\n").length;
    }
}
