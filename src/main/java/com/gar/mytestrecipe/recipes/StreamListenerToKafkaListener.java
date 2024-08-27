package com.gar.mytestrecipe.recipes;

import org.openrewrite.ExecutionContext;
import org.openrewrite.Recipe;
import org.openrewrite.TreeVisitor;
import org.openrewrite.java.ChangeType;
import org.openrewrite.java.JavaTemplate;
import org.openrewrite.java.JavaVisitor;
import org.openrewrite.java.search.FindAnnotations;
import org.openrewrite.java.tree.J;
import org.openrewrite.java.tree.JavaType;

public class StreamListenerToKafkaListener extends Recipe {

    private static final String STREAM_LISTENER_FQN = "org.springframework.cloud.stream.annotation.StreamListener";
    private static final String KAFKA_LISTENER_FQN = "org.springframework.kafka.annotation.KafkaListener";

    @Override
    public String getDisplayName() {
        return "Convert @StreamListener to @KafkaListener";
    }

    @Override
    public String getDescription() {
        return "Converts Spring Cloud Stream @StreamListener annotations to Spring Kafka @KafkaListener annotations.";
    }

    @Override
    public Recipe doNext(Recipe recipe) {
        return recipe
                .doNext(new FindAnnotations(STREAM_LISTENER_FQN, false))
                .doNext(new ChangeAnnotationTemplate())
                .doNext(new ChangeType(STREAM_LISTENER_FQN, KAFKA_LISTENER_FQN, true));
    }

    private static class ChangeAnnotationTemplate extends Recipe {
        @Override
        public String getDisplayName() {
            return "Change @StreamListener to @KafkaListener structure";
        }

        @Override
        public String getDescription() {
            return "Changes the structure of @StreamListener annotations to match @KafkaListener.";
        }

        @Override
        protected TreeVisitor<?, ExecutionContext> getVisitor() {
            return new JavaVisitor<ExecutionContext>() {
                @Override
                public J visitAnnotation(J.Annotation annotation, ExecutionContext ctx) {
                    if (isStreamListenerAnnotation(annotation)) {
                        return annotation.withTemplate(
                                JavaTemplate
                                        .builder(this::getCursor,"@KafkaListener")
                                        .imports(KAFKA_LISTENER_FQN)
                                        .build(),
                                annotation.getCoordinates().replace(),
                                annotation.getArguments().get(0)
                        );
                    }
                    return super.visitAnnotation(annotation, ctx);
                }

                private boolean isStreamListenerAnnotation(J.Annotation annotation) {
                    return annotation.getType() != null
                            && annotation.getType() instanceof JavaType.FullyQualified
                            && STREAM_LISTENER_FQN.equals(((JavaType.FullyQualified) annotation.getType()).getFullyQualifiedName());
                }
            };
        }
    }
}