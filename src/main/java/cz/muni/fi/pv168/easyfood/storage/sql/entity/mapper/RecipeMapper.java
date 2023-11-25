package cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper;

import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.storage.DataStorageException;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.CategoryEntity;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.IngredientWithAmountEntity;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.RecipeEntity;

import java.util.List;
import java.util.ArrayList;

/**
 * Mapper from the {@link RecipeEntity} to {@link Recipe}.
 */
public class RecipeMapper implements EntityMapper<RecipeEntity, Recipe> {
    private final DataAccessObject<CategoryEntity> categoryDao;
    private final EntityMapper<CategoryEntity, Category> categoryManager;
    private final DataAccessObject<IngredientWithAmountEntity> ingredientDao;
    private final EntityMapper<IngredientWithAmountEntity, IngredientWithAmount> ingredientManager;


    public RecipeMapper(
            DataAccessObject<CategoryEntity> categoryDao,
            EntityMapper<CategoryEntity, Category> categoryManager,
            DataAccessObject<IngredientWithAmountEntity> ingredientDao,
            EntityMapper<IngredientWithAmountEntity, IngredientWithAmount> ingredientManager
    ) {
        this.categoryDao = categoryDao;
        this.categoryManager = categoryManager;
        this.ingredientDao = ingredientDao;
        this.ingredientManager = ingredientManager;
    }

    @Override
    public Recipe mapToBusiness(RecipeEntity entity) {
        var category = categoryDao
                .findById(entity.categoryId())
                .map(categoryManager::mapToBusiness)
                .orElseThrow(() -> new DataStorageException("Category id not found id: " +
                        entity.categoryId()));

        List<IngredientWithAmount> ingredients = new ArrayList<>();
        for (Long id : entity.ingredients()) {
            var ingredient = ingredientDao
                    .findById(id)
                    .map(ingredientManager::mapToBusiness)
                    .orElseThrow(() -> new DataStorageException("Category id not found id: " +
                            entity.categoryId()));
            ingredients.add(ingredient);
        }

        return new Recipe(
                entity.guid(),
                entity.name(),
                ingredients,
                entity.description(),
                entity.preparationTime(),
                entity.portions(),
                category
        );

    }

    @Override
    public RecipeEntity mapNewEntityToDatabase(Recipe entity) {
        var category = categoryDao
                .findByGuid(entity.getCategory().getGuid())
                .orElseThrow(() -> new DataStorageException("Category not found, guid: " +
                        entity.getCategory().getGuid()));

        List<Long> ingredientIds = new ArrayList<>();
        for (IngredientWithAmount ingredient : entity.getIngredients()) {
            var ingredientEntity = ingredientDao
                    .findByGuid(ingredient.getIngredient().getGuid())
                    .orElseThrow(() -> new DataStorageException("Ingredient not found, guid: " +
                            ingredient.getIngredient().getGuid()));
            ingredientIds.add(ingredientEntity.id());
        }

        return new RecipeEntity(
                entity.getGuid(),
                entity.getName(),
                ingredientIds,
                entity.getDescription(),
                entity.getPreparationTime(),
                entity.getPortions(),
                category.id()
        );
    }

    @Override
    public RecipeEntity mapExistingEntityToDatabase(Recipe entity, Long dbId) {
        var category = categoryDao
                .findByGuid(entity.getCategory().getGuid())
                .orElseThrow(() -> new DataStorageException("Category not found, guid: " +
                        entity.getCategory().getGuid()));

        List<Long> ingredientIds = new ArrayList<>();
        for (IngredientWithAmount ingredient : entity.getIngredients()) {
            var ingredientEntity = ingredientDao
                    .findByGuid(ingredient.getIngredient().getGuid())
                    .orElseThrow(() -> new DataStorageException("Ingredient not found, guid: " +
                            ingredient.getIngredient().getGuid()));
            ingredientIds.add(ingredientEntity.id());
        }

        return new RecipeEntity(
                dbId,
                entity.getGuid(),
                entity.getName(),
                ingredientIds,
                entity.getDescription(),
                entity.getPreparationTime(),
                entity.getPortions(),
                category.id()
        );
    }
}
