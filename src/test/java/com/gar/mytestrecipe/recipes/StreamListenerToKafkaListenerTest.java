package com.gar.mytestrecipe.recipes;

import org.junit.jupiter.api.Test;
import org.openrewrite.Recipe;
import org.openrewrite.test.RewriteTest;
import org.openrewrite.java.JavaParser;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.java;

class StreamListenerToKafkaListenerTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new StreamListenerToKafkaListener())
                .parser(JavaParser.fromJavaVersion()
                        .classpath("spring-cloud-stream", "spring-kafka"));
    }

    @Test
    void convertsStreamListenerToKafkaListener() {
        rewriteRun(
                java(
                        """
                        import org.springframework.cloud.stream.annotation.StreamListener;
                        
                        class MyClass {
                            @StreamListener("myTopic")
                            public void handleMessage(String message) {
                                // Handle the message
                            }
                        }
                        """,
                        """
                        import org.springframework.kafka.annotation.KafkaListener;
                        
                        class MyClass {
                            @KafkaListener(topics = "#{myTopic}")
                            public void handleMessage(String message) {
                                // Handle the message
                            }
                        }
                        """
                )
        );
    }

    @Test
    void doesNotChangeOtherAnnotations() {
        rewriteRun(
                java(
                        """
                        import org.springframework.stereotype.Component;
                        
                        @Component
                        class MyClass {
                            public void someMethod() {
                                // Some code
                            }
                        }
                        """
                )
        );
    }

    @Test
    void handlesMultipleAnnotations() {
        rewriteRun(
                java(
                        """
                        import org.springframework.cloud.stream.annotation.StreamListener;
                        import org.springframework.stereotype.Component;
                        
                        @Component
                        class MyClass {
                            @StreamListener
                            public void handleMessage1(String message) {
                                // Handle the message
                            }
                            
                            @StreamListener
                            public void handleMessage2(String message) {
                                // Handle the message
                            }
                        }
                        """,
                        """
                        import org.springframework.kafka.annotation.KafkaListener;
                        import org.springframework.stereotype.Component;
                        
                        @Component
                        class MyClass {
                            @KafkaListener(topics = "#{topic1}")
                            public void handleMessage1(String message) {
                                // Handle the message
                            }
                            
                            @KafkaListener(topics = "#{topic2}")
                            public void handleMessage2(String message) {
                                // Handle the message
                            }
                        }
                        """
                )
        );
    }
}
