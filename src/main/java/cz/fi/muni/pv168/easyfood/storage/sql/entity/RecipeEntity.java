package cz.fi.muni.pv168.easyfood.storage.sql.entity;

import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.IngredientWithAmount;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public record RecipeEntity(
        Long id,
        String guid,
        String name,
        List<IngredientWithAmount> ingredients,
        String description,
        int preparationTime,
        int portions,
        Category category
) {
    public RecipeEntity(
            Long id,
            String guid,
            String name,
            List<IngredientWithAmount> ingredients,
            String description,
            int preparationTime,
            int portions,
            Category category
    ) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.ingredients = Objects.requireNonNull(ingredients, "ingredients must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");
        this.preparationTime = preparationTime;
        this.portions = portions;
        this.category = Objects.requireNonNull(category, "category must not be null");
    }

    public RecipeEntity(
            String guid,
            String name,
            List<IngredientWithAmount> ingredients,
            String description,
            int preparationTime,
            int portions,
            Category category
    ) {
        this(null, guid, name, ingredients, description, preparationTime, portions, category);
    }

    public static RecipeEntity createEmptyRecipeEntity() {
        return new RecipeEntity(null, "", new ArrayList<>(), "", 0, 0, null);
    }
}
