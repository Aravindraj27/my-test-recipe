package com.gar.mytestrecipe.recipes;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.java.Assertions.java;

class ConvertPojoToRecordRecipeTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new ConvertPojoToRecordRecipe());
    }

    @Test
    void convertPojoToRecord() {
        rewriteRun(
                java(
                        """
                        package com.example.model;
                        
                        public class User {
                            private String name;
                            private int age;
                            private String email;
                            
                            public User(String name, int age, String email) {
                                this.name = name;
                                this.age = age;
                                this.email = email;
                            }
                            
                            public String getName() {
                                return name;
                            }
                            
                            public int getAge() {
                                return age;
                            }
                            
                            public String getEmail() {return email;}
                        }
                        """,
                        """
                        package com.example.model;
                        
                        public record User (java.lang.String name, int age, java.lang.String email) {
                        }
                        """
                )
        );
    }

    @Test
    void doNotConvertNonTargetPackageClass() {
        rewriteRun(
                java(
                        """
                        package com.example.service;
                        
                        public class UserService {
                            private String name;
                            
                            public UserService(String name) {
                                this.name = name;
                            }
                            
                            public String getName() {
                                return name;
                            }
                        }
                        """
                )
        );
    }

    @Test
    void doNotConvertAbstractClass() {
        rewriteRun(
                java(
                        """
                        package com.example.model;
                        
                        public abstract class AbstractUser {
                            private String name;
                            
                            public AbstractUser(String name) {
                                this.name = name;
                            }
                            
                            public String getName() {
                                return name;
                            }
                        }
                        """
                )
        );
    }
}