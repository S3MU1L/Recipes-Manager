package cz.fi.muni.pv168.easyfood.storage.sql.entity.mapper;

import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.IngredientWithAmount;
import cz.fi.muni.pv168.easyfood.storage.DataStorageException;
import cz.fi.muni.pv168.easyfood.storage.sql.dao.DataAccessObject;
import cz.fi.muni.pv168.easyfood.storage.sql.entity.IngredientEntity;
import cz.fi.muni.pv168.easyfood.storage.sql.entity.IngredientWithAmountEntity;

public class IngredientWitAmountMapper implements EntityMapper<IngredientWithAmountEntity, IngredientWithAmount> {

    private final DataAccessObject<IngredientEntity> ingredientDao;
    private final EntityMapper<IngredientEntity, Ingredient> ingredientManager;

    public IngredientWitAmountMapper(
            DataAccessObject<IngredientEntity> ingredientDao,
            EntityMapper<IngredientEntity, Ingredient> ingredientManager) {
        this.ingredientDao = ingredientDao;
        this.ingredientManager = ingredientManager;
    }


    @Override
    public IngredientWithAmount mapToBusiness(IngredientWithAmountEntity entity) {
        var ingredient = ingredientDao
                .findById(entity.ingredientId())
                .map(ingredientManager::mapToBusiness)
                .orElseThrow(() -> new DataStorageException("Ingredient id not found id: " +
                        entity.ingredientId()));
        return new IngredientWithAmount(
                entity.guid(),
                ingredient,
                entity.amount()
        );
    }

    @Override
    public IngredientWithAmountEntity mapNewEntityToDatabase(IngredientWithAmount entity) {
        var ingredientEntity = ingredientDao
                .findByGuid(entity.getIngredient().getGuid())
                .orElseThrow(() -> new DataStorageException("Ingredient not found, guid: " +
                        entity.getIngredient().getGuid()));

        return new IngredientWithAmountEntity(
                entity.getGuid(),
                ingredientEntity.id(),
                entity.getAmount()
        );
    }

    @Override
    public IngredientWithAmountEntity mapExistingEntityToDatabase(IngredientWithAmount entity, Long dbId) {
        var ingredientEntity = ingredientDao
                .findByGuid(entity.getIngredient().getGuid())
                .orElseThrow(() -> new DataStorageException("Ingredient not found, guid: " +
                        entity.getIngredient().getGuid()));

        return new IngredientWithAmountEntity(
                dbId,
                entity.getGuid(),
                ingredientEntity.id(),
                entity.getAmount()
        );
    }
}
