//package com.gar.mytestrecipe.recipes;
//package org.openrewrite.java;
//
//import org.openrewrite.ExecutionContext;
//import org.openrewrite.Recipe;
//import org.openrewrite.TreeVisitor;
//import org.openrewrite.java.JavaIsoVisitor;
//import org.openrewrite.java.tree.J;
//import org.openrewrite.java.tree.JavaType;
//import org.openrewrite.java.tree.Space;
//import org.openrewrite.marker.Markers;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AddModelMapperConfig extends Recipe {
//
//    @Override
//    public String getDisplayName() {
//        return "Add ModelMapper Configuration";
//    }
//
//    @Override
//    public String getDescription() {
//        return "Adds a configuration class that creates a ModelMapper bean with basic configuration.";
//    }
//
//    @Override
//    protected TreeVisitor<?, ExecutionContext> getVisitor() {
//        return new JavaIsoVisitor<ExecutionContext>() {
//            @Override
//            public J.CompilationUnit visitCompilationUnit(J.CompilationUnit cu, ExecutionContext executionContext) {
//                if (cu.getPackageDeclaration() == null) {
//                    return cu;
//                }
//
//                List<J.Import> imports = new ArrayList<>(cu.getImports());
//                addImportIfNotPresent(imports, "org.modelmapper.ModelMapper");
//                addImportIfNotPresent(imports, "org.springframework.context.annotation.Bean");
//                addImportIfNotPresent(imports, "org.springframework.context.annotation.Configuration");
//                cu = cu.withImports(imports);
//
//                // Create the ModelMapperConfig class
//                J.ClassDeclaration newClass = createModelMapperConfigClass();
//
//                List<J.ClassDeclaration> classes = new ArrayList<>(cu.getClasses());
//                classes.add(newClass);
//                return cu.withClasses(classes);
//            }
//
//            private void addImportIfNotPresent(List<J.Import> imports, String fullyQualifiedName) {
//                boolean importExists = imports.stream()
//                        .anyMatch(i -> i.getQualid().print().equals(fullyQualifiedName));
//                if (!importExists) {
//                    imports.add(createImport(fullyQualifiedName));
//                }
//            }
//
//            private J.Import createImport(String fullyQualifiedName) {
//                String[] parts = fullyQualifiedName.split("\\.");
//                J.Identifier packageName = J.Identifier.build(randomId(), Space.EMPTY, Markers.EMPTY,
//                        String.join(".", parts, 0, parts.length - 1));
//                J.Identifier className = J.Identifier.build(randomId(), Space.EMPTY, Markers.EMPTY,
//                        parts[parts.length - 1]);
//                J.FieldAccess qualid = new J.FieldAccess(randomId(), Space.EMPTY, Markers.EMPTY, packageName, className);
//                return new J.Import(randomId(), Space.EMPTY, Markers.EMPTY, false, qualid);
//            }
//
//            private J.ClassDeclaration createModelMapperConfigClass() {
//                J.Modifier configAnnotation = new J.Annotation(
//                        randomId(),
//                        Space.EMPTY,
//                        Markers.EMPTY,
//                        J.Identifier.build(randomId(), Space.EMPTY, Markers.EMPTY, "Configuration", JavaType.ShallowClass.build("org.springframework.context.annotation.Configuration"))
//                );
//
//                J.ClassDeclaration classDeclaration = J.ClassDeclaration.build(
//                        randomId(),
//                        Space.build("\n"),
//                        Markers.EMPTY,
//                        List.of(configAnnotation),
//                        List.of(),
//                        null,
//                        J.Identifier.build(randomId(), Space.EMPTY, Markers.EMPTY, "ModelMapperConfig"),
//                        null,
//                        null,
//                        null,
//                        J.Block.build(
//                                randomId(),
//                                Space.EMPTY,
//                                Markers.EMPTY,
//                                List.of(createModelMapperMethod()),
//                                Space.EMPTY
//                        )
//                );
//
//                return classDeclaration;
//            }
//
//            private J.MethodDeclaration createModelMapperMethod() {
//                J.Modifier beanAnnotation = new J.Annotation(
//                        randomId(),
//                        Space.EMPTY,
//                        Markers.EMPTY,
//                        J.Identifier.build(randomId(), Space.EMPTY, Markers.EMPTY, "Bean", JavaType.ShallowClass.build("org.springframework.context.annotation.Bean"))
//                );
//
//                return J.MethodDeclaration.build(
//                        randomId(),
//                        Space.build("\n    "),
//                        Markers.EMPTY,
//                        List.of(beanAnnotation, J.Modifier.Public.INSTANCE),
//                        null,
//                        List.of(),
//                        J.Identifier.build(randomId(), Space.EMPTY, Markers.EMPTY, "ModelMapper"),
//                        J.Identifier.build(randomId(), Space.EMPTY, Markers.EMPTY, "modelMapper"),
//                        List.of(),
//                        null,
//                        J.Block.build(
//                                randomId(),
//                                Space.EMPTY,
//                                Markers.EMPTY,
//                                List.of(
//                                        // You would add the method body here
//                                        // This is simplified for brevity
//                                ),
//                                Space.EMPTY
//                        )
//                );
//            }
//        };
//    }
//
//    // You need to implement this method to generate unique IDs
//    private String randomId() {
//        // Implementation depends on your OpenRewrite version
//        // For example, you might use UUID.randomUUID().toString()
//        return ""; // Placeholder implementation
//    }
//}