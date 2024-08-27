## Open Rewrite
* OpenRewrite is an automated refactoring ecosystem for source code, enabling developers to effectively eliminate technical debt within their repositories.
* OpenRewrite works by making changes to Lossless Semantic Trees (LSTs) that represent your source code and printing the modified trees back into source code. You can then review the changes in your code and commit the results. Modifications to the LST are performed in Visitors and visitors are aggregated into Recipes. OpenRewrite recipes make minimally invasive changes to your source code that honor the original formatting.

## Recipes
* Recipes are used in plugins that would define how to manipulate and change the LSTs to make our desired changes without affecting the syntax and semantics of the existing code.
* There are three types of recipes in Open Rewrite
  * Declarative recipes
  * Refaster Template Recipe
  * Imperative Recipe

### Declarative Recipe
* Declarative recipes are the simplest and most common recipes. They are entirely written in YAML, and they generally tie together existing recipes while adding some light configuration to them.
* In this project we have defined an update upgrade Jackson version recipe that will update any current jackson version to 29.x
* The amount of changes we can do on the code with this recipe is limited like updating the import statements or updating the pom files.

### Refaster template recipes
* Refaster template recipes are the "middle ground" of recipes. They offer more functionality than declarative recipes, but not as much as imperative recipes. On the other hand, compared to an imperative recipe, they're much quicker to create and require much less knowledge to get started.
* Refaster template recipes can only be used to replace one expression with another – or one statement with another. Because of that, they're ideal for straightforward replacements such as converting ``StringUtils.equals(..)`` to ``Objects.equals(..)``. These are more than just a string replacement, though; they offer compiler and type support.

### Imperative Recipes
* Imperative recipes offer the most freedom and functionality at the cost of being more difficult to create. They allow you to write Java code to implement your recipe.
* Imperative recipes are ideal for situations where there is a lot of complexity or nuance needed to determine what should be changed or what it should be changed into. For instance, if you wanted to write a recipe that added the ``final`` modifier to any local variables that aren't reassigned, you would need to create an imperative recipe as refaster templates don't have the ability to determine whether a variable has been reassigned.
* Here we have defined multiple Imperative recipes like Annotation updates, POJO to Record updates.
* Inorder to write Imperative or any other recipe a detailed knowledge of the LSTs are required since we would be manipulating tha java code.

## LST
* A Lossless Semantic Tree (LST) is a tree representation of code. Unlike the traditional Abstract Syntax Tree (AST), OpenRewrite's LST offers a unique set of characteristics that make it possible to perform accurate transformations and searches across a repository
  * **Type-attributed**: Each LST is imbued with type information. For example, when referencing a field, the source code may just refer to it as myField. The OpenRewrite LST for myField, on the other hand, would contain additional information about what the type of myField is, even if it isn't defined in the same source file or even the same project.
  * **Format-preserving** Whitespace before and after LSTs are preserved in the tree so the tree can be printed out to reconstitute the original source code without clobbering formatting. Additionally, refactoring operations that insert code are sensitive to the local style of the code around them and match the local style.
* The LST uses a visitor pattern to find and modify the code so where ever we use the recipe abstract class we need to override the `getVistor` method to manipulate the code. 
* A Detailed Explanation of the Java LST is available [here](https://docs.openrewrite.org/concepts-explanations/lst-examples).
* In this specific project we have used the Class declaration LST to manipulate the code in recognising all java classes under the package ``org.example.model`` and converting it into record this will include manipulating all the get and set methods in that class.
* We have also used an Annotation visitor to convert the Stream listener in to a kafka Listener and also update the import statements [WIP this is not working as expected in process of fixing it].
* The [J Interface](https://javadoc.io/static/org.openrewrite/rewrite-java/7.36.1/org/openrewrite/java/tree/J.html) will contain all the declaration in the above-mentioned LST Document.

## Setting Up the project for Open rewrite
### Requirements
1. Java 8 or above.
2. Maven 3.2+
3. Intellij IDE is preferred but any other IDE would work.

### Creating a Recipe
* Execute the following command to set up a maven project in your local
```shell
mvn -B archetype:generate -DgroupId=com.mycompany.app -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4
```
* Here we are using open rewrite version ``7.38.0``
* Java Version Used - 17

### Folder Structure
* ``src/main/java`` - we write all the Imperative recipes.
* ``src/test/java`` - test cases for all the Imperative recipes.
* ``src/main/resources/META-INF/rewrite`` - We have all the declarative recipes here, and we can compile all the declarative, Imeprative and the Refaster Templates here. We have used the ``recipes.yml`` for the same purpose.

### Executing test cases
* Once the test cases are in place for the imperative recipe we can start executing the unit test case in place for the same.
* In order for us to run the receipe we need to publish the recipe and we need to add the following plugin to recipe
```xml
<project>
    <build>
        <plugins>
            <plugin>
                <groupId>org.openrewrite.maven</groupId>
                <artifactId>rewrite-maven-plugin</artifactId>
                <version>5.39.0</version>
                <configuration>
                    <activeRecipes>
                        <recipe> [your recipe name] </recipe>
                    </activeRecipes>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId> [your recipe module's groupId] </groupId>
                        <artifactId> [your recipe module's artifactId] </artifactId>
                        <version> [your recipe module's version] </version>
                    </dependency>
                </dependencies>
            </plugin>
        </plugins>
    </build>
</project>
```
* Once this has been added to the target project we need to execute the following commands
```shell
mvn rewrite:run
mvn rewrite:dryRun
```

#### Sources
* Most of the content in the document can be found on the official [Moderne Open rewrite](https://docs.openrewrite.org/) site
