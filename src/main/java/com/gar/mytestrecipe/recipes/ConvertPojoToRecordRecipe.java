package com.gar.mytestrecipe.recipes;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.JavaIsoVisitor;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;

import java.util.List;
import java.util.stream.Collectors;

public class ConvertPojoToRecordRecipe extends Recipe {
    @Override
    public String getDisplayName() {
        return "Convert POJOs to Records";
    }

    @Override
    public String getDescription() {
        return "Converts normal POJO classes in the model package to Java records.";
    }

    @Override
    protected TreeVisitor<?, ExecutionContext> getVisitor() {
        return new JavaIsoVisitor<ExecutionContext>() {
            @Override
            public J.ClassDeclaration visitClassDeclaration(J.ClassDeclaration classDecl, ExecutionContext ctx) {
                J.ClassDeclaration cd = super.visitClassDeclaration(classDecl, ctx);

                if (cd.getType() != null &&
                        cd.getType().getFullyQualifiedName().startsWith("com.example.model") &&
                        !isRecord(cd) &&
                        !cd.hasModifier(J.Modifier.Type.Abstract)) {

                    List<J.VariableDeclarations> fields = cd.getBody().getStatements().stream()
                            .filter(statement -> statement instanceof J.VariableDeclarations)
                            .map(statement -> (J.VariableDeclarations) statement)
                            .filter(vd -> vd.hasModifier(J.Modifier.Type.Private))
                            .collect(Collectors.toList());

                    String recordComponents = fields.stream()
                            .map(vd -> {
                                JavaType.FullyQualified type = vd.getType() instanceof JavaType.FullyQualified ?
                                        (JavaType.FullyQualified) vd.getType() : null;
                                String typeName = type != null ? type.getFullyQualifiedName() : vd.getType().toString();
                                return typeName + " " + vd.getVariables().get(0).getSimpleName();
                            })
                            .collect(Collectors.joining(", "));

                    JavaTemplate recordTemplate = JavaTemplate.builder(this::getCursor, "public record #{} (#{}) {}")
                            .build();

                    cd = cd.withTemplate(recordTemplate, cd.getCoordinates().replace(),
                            cd.getSimpleName(), recordComponents);

                    cd = cd.withKind(J.ClassDeclaration.Kind.Type.Record);
                    cd = cd.withBody(cd.getBody().withStatements(List.of()));

                    maybeAddImport("java.lang.Record");
                }

                return cd;
            }

            private boolean isRecord(J.ClassDeclaration classDecl) {
                return classDecl.getKind() == J.ClassDeclaration.Kind.Type.Record;
            }
        };
    }
}