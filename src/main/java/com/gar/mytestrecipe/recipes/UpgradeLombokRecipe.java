package com.gar.mytestrecipe.recipes;

import org.openrewrite.Recipe;
import org.openrewrite.maven.UpgradeDependencyVersion;

import java.util.ArrayList;

public class UpgradeLombokRecipe extends Recipe {
    @Override
    public String getDisplayName() {
        return "Upgrade Lombok to 1.18.32";
    }

    @Override
    public String getDescription() {
        return "Upgrades the Lombok dependency from version 1.18.22 to 1.18.32.";
    }

    public UpgradeLombokRecipe() {
        doNext(new UpgradeDependencyVersion("org.projectlombok", "lombok", "1.18.32", null, false, new ArrayList<>()));
    }
}
