package cz.fi.muni.pv168.easyfood.storage.sql.entity;

import java.util.List;
import java.util.Objects;

public record RecipeEntity
        (
                Long id,
                String guid,
                String name,
                List<Long> ingredients,
                String description,
                int preparationTime,
                int portions,
                Long categoryId
        ) {
    public RecipeEntity(
            Long id,
            String guid,
            String name,
            List<Long> ingredients,
            String description,
            int preparationTime,
            int portions,
            Long categoryId
    ) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.ingredients = Objects.requireNonNull(ingredients, "ingredients must not be null");
        this.description = Objects.requireNonNull(description, "description must not be null");
        this.preparationTime = preparationTime;
        this.portions = portions;
        this.categoryId = Objects.requireNonNull(categoryId, "categoryId must not be null");
    }

    public RecipeEntity(
            String guid,
            String name,
            List<Long> ingredients,
            String description,
            int preparationTime,
            int portions,
            Long categoryId
    ) {
        this(null, guid, name, ingredients, description, preparationTime, portions, categoryId);
    }

}
