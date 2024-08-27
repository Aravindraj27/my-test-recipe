package com.gar.mytestrecipe.recipes;

import org.junit.jupiter.api.Test;
import org.openrewrite.test.RecipeSpec;
import org.openrewrite.test.RewriteTest;

import static org.openrewrite.maven.Assertions.pomXml;

class UpgradeLombokRecipeTest implements RewriteTest {

    @Override
    public void defaults(RecipeSpec spec) {
        spec.recipe(new UpgradeLombokRecipe());
    }

    @Test
    void upgradeLombokVersion() {
        rewriteRun(
                pomXml(
                        """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project>
                          <modelVersion>4.0.0</modelVersion>
                          <groupId>com.example</groupId>
                          <artifactId>lombok-test</artifactId>
                          <version>1.0.0</version>
                          <dependencies>
                            <dependency>
                              <groupId>org.projectlombok</groupId>
                              <artifactId>lombok</artifactId>
                              <version>1.18.22</version>
                            </dependency>
                          </dependencies>
                        </project>
                        """,
                        """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project>
                          <modelVersion>4.0.0</modelVersion>
                          <groupId>com.example</groupId>
                          <artifactId>lombok-test</artifactId>
                          <version>1.0.0</version>
                          <dependencies>
                            <dependency>
                              <groupId>org.projectlombok</groupId>
                              <artifactId>lombok</artifactId>
                              <version>1.18.32</version>
                            </dependency>
                          </dependencies>
                        </project>
                        """
                )
        );
    }

    @Test
    void upgradeLombokVersionFromDiffVersion() {
        rewriteRun(
                pomXml(
                        """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project>
                          <modelVersion>4.0.0</modelVersion>
                          <groupId>com.example</groupId>
                          <artifactId>lombok-test</artifactId>
                          <version>1.0.0</version>
                          <dependencies>
                            <dependency>
                              <groupId>org.projectlombok</groupId>
                              <artifactId>lombok</artifactId>
                              <version>1.18.18</version>
                            </dependency>
                          </dependencies>
                        </project>
                        """,
                        """
                        <?xml version="1.0" encoding="UTF-8"?>
                        <project>
                          <modelVersion>4.0.0</modelVersion>
                          <groupId>com.example</groupId>
                          <artifactId>lombok-test</artifactId>
                          <version>1.0.0</version>
                          <dependencies>
                            <dependency>
                              <groupId>org.projectlombok</groupId>
                              <artifactId>lombok</artifactId>
                              <version>1.18.32</version>
                            </dependency>
                          </dependencies>
                        </project>
                        """
                )
        );
    }
}
