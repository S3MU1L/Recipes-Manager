package cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper;

import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.storage.DataStorageException;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.IngredientEntity;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.RecipeEntity;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.UnitEntity;

/**
 * Mapper from the {@link RecipeEntity} to {@link Recipe}.
 */
public class IngredientMapper implements EntityMapper<IngredientEntity, Ingredient> {

    private final DataAccessObject<UnitEntity> unitDao;
    private final EntityMapper<UnitEntity, Unit> unitManager;

    public IngredientMapper(
            DataAccessObject<UnitEntity> unitDao,
            EntityMapper<UnitEntity, Unit> unitManager) {
        this.unitDao = unitDao;
        this.unitManager = unitManager;
    }

    @Override
    public Ingredient mapToBusiness(IngredientEntity entity) {
        var unit = unitDao
                .findById(entity.unitId())
                .map(unitManager::mapToBusiness)
                .orElseThrow(() -> new DataStorageException("Unit id not found id: " +
                        entity.unitId()));

        return new Ingredient(
                entity.guid(),
                entity.name(),
                entity.calories(),
                unit
        );
    }

    @Override
    public IngredientEntity mapNewEntityToDatabase(Ingredient entity) {
        var unitEntity = unitDao
                .findByGuid(entity.getUnit().getGuid())
                .orElseThrow(() -> new DataStorageException("Unit not found, guid: " +
                        entity.getUnit().getGuid()));

        return new IngredientEntity(
                entity.getGuid(),
                unitEntity.id(),
                entity.getName(),
                entity.getCalories()
        );
    }

    @Override
    public IngredientEntity mapExistingEntityToDatabase(Ingredient entity, Long dbId) {
        var unitEntity = unitDao
                .findByGuid(entity.getUnit().getGuid())
                .orElseThrow(() -> new DataStorageException("Unit not found, guid: " +
                        entity.getUnit().getGuid()));

        return new IngredientEntity(
                dbId,
                entity.getGuid(),
                unitEntity.id(),
                entity.getName(),
                entity.getCalories()
        );
    }
}
