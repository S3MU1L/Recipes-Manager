package cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper;

import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.storage.DataStorageException;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.IngredientWithAmountDao;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.CategoryEntity;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.IngredientEntity;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.IngredientWithAmountEntity;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.RecipeEntity;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.ArrayList;

/**
 * Mapper from the {@link RecipeEntity} to {@link Recipe}.
 */
public class RecipeMapper implements EntityMapper<RecipeEntity, Recipe> {
    private final DataAccessObject<CategoryEntity> categoryDao;
    private final EntityMapper<CategoryEntity, Category> categoryManager;
    private final DataAccessObject<IngredientWithAmountEntity> ingredientWithAmountDao;
    private final EntityMapper<IngredientWithAmountEntity, IngredientWithAmount> ingredientManager;


    public RecipeMapper(
            DataAccessObject<CategoryEntity> categoryDao,
            EntityMapper<CategoryEntity, Category> categoryManager,
            IngredientWithAmountDao ingredientWithAmountDao,
            EntityMapper<IngredientWithAmountEntity, IngredientWithAmount> ingredientManager
    ) {
        this.categoryDao = categoryDao;
        this.categoryManager = categoryManager;
        this.ingredientWithAmountDao = ingredientWithAmountDao;
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

        for (Long ingredientId : entity.ingredients()) {
            var ingredientEntity = ingredientWithAmountDao.findByRecipeAndIngredientId(entity.id(), ingredientId);
            IngredientWithAmount ingredientWithAmount = ingredientManager.mapToBusiness(ingredientEntity.get());
            System.out.println(ingredientWithAmount);
            ingredients.add(ingredientWithAmount);
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

        return new RecipeEntity(
                entity.getGuid(),
                entity.getName(),
                new ArrayList<>(),
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

        return new RecipeEntity(
                dbId,
                entity.getGuid(),
                entity.getName(),
                new ArrayList<>(),
                entity.getDescription(),
                entity.getPreparationTime(),
                entity.getPortions(),
                category.id()
        );
    }
}
