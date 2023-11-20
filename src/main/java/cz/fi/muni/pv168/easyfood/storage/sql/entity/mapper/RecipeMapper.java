package cz.fi.muni.pv168.easyfood.storage.sql.entity.mapper;

import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.storage.sql.entity.RecipeEntity;

/**
 * Mapper from the {@link RecipeEntity} to {@link Recipe}.
 */
public class RecipeMapper implements EntityMapper<RecipeEntity, Recipe> {
    @Override
    public Recipe mapToBusiness(RecipeEntity entity) {
        return new Recipe(
                entity.guid(),
                entity.name(),
                entity.ingredients(),
                entity.description(),
                entity.preparationTime(),
                entity.portions(),
                entity.category()
        );
    }

    @Override
    public RecipeEntity mapNewEntityToDatabase(Recipe entity) {
        return new RecipeEntity(
                entity.getGuid(),
                entity.getName(),
                entity.getIngredients(),
                entity.getDescription(),
                entity.getPreparationTime(),
                entity.getPortions(),
                entity.getCategory()
        );
    }

    @Override
    public RecipeEntity mapExistingEntityToDatabase(Recipe entity, Long dbId) {
        return new RecipeEntity(
                dbId,
                entity.getGuid(),
                entity.getName(),
                entity.getIngredients(),
                entity.getDescription(),
                entity.getPreparationTime(),
                entity.getPortions(),
                entity.getCategory()
        );
    }
}
