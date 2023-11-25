package cz.fi.muni.pv168.easyfood.storage.sql;

import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.repository.Repository;
import cz.fi.muni.pv168.easyfood.storage.DataStorageException;
import cz.fi.muni.pv168.easyfood.storage.sql.dao.DataAccessObject;
import cz.fi.muni.pv168.easyfood.storage.sql.entity.IngredientEntity;
import cz.fi.muni.pv168.easyfood.storage.sql.entity.mapper.EntityMapper;

import java.util.List;
import java.util.Optional;

public class IngredientSqlRepository implements Repository<Ingredient> {

    private final DataAccessObject<IngredientEntity> ingredientDao;
    private final EntityMapper<IngredientEntity, Ingredient> ingredientMapper;

    public IngredientSqlRepository(
            DataAccessObject<IngredientEntity> recipeDao,
            EntityMapper<IngredientEntity, Ingredient> categoryMapper) {
        this.ingredientDao = recipeDao;
        this.ingredientMapper = categoryMapper;
    }

    @Override
    public List<Ingredient> findAll() {
        return ingredientDao
                .findAll()
                .stream()
                .map(ingredientMapper::mapToBusiness)
                .toList();
    }

    @Override
    public void create(Ingredient newEntity) {
        ingredientDao.create(ingredientMapper.mapNewEntityToDatabase(newEntity));
    }

    @Override
    public void update(Ingredient entity) {
        var existingRecipe = ingredientDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Recipe not found, id: " + entity.getGuid()));
        var updatedRecipe = ingredientMapper.mapExistingEntityToDatabase(entity, existingRecipe.id());

        ingredientDao.update(updatedRecipe);
    }

    @Override
    public void deleteByGuid(String id) {
        ingredientDao.deleteByGuid(id);
    }

    @Override
    public void deleteAll() {
        ingredientDao.deleteAll();
    }

    @Override
    public boolean existsByGuid(String id) {
        return ingredientDao.existsByGuid(id);
    }

    @Override
    public Optional<Ingredient> findByGuid(String id) {
        return ingredientDao
                .findByGuid(id)
                .map(ingredientMapper::mapToBusiness);
    }
}
