package cz.fi.muni.pv168.easyfood.storage.sql;

import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.bussiness.repository.Repository;
import cz.fi.muni.pv168.easyfood.storage.sql.dao.DataAccessObject;
import cz.fi.muni.pv168.easyfood.storage.sql.entity.RecipeEntity;

import java.util.List;
import java.util.Optional;

public class RecipeSqlRepository implements Repository<Recipe> {

    private final DataAccessObject<RecipeEntity> recipeDao;
    private final EntityMapper<RecipeEntity, Recipe> recipeMapper;

    public RecipeSqlRepository(
            DataAccessObject<RecipeEntity> recipeDao,
            EntityMapper<RecipeEntity, Recipe> recipeMapper) {
        this.recipeDao = recipeDao;
        this.recipeMapper = recipeMapper;
    }

    @Override
    public List<Recipe> findAll() {
        return recipeDao
                .findAll()
                .stream()
                .map(recipeMapper::mapToBusiness)
                .toList();
    }

    @Override
    public void create(Recipe newEntity) {
        recipeDao.create(recipeMapper.mapNewEntityToDatabase(newEntity));
    }

    @Override
    public void update(Recipe entity) {
        var existingRecipe = recipeDao.findByGuid(entity.getId())
                .orElseThrow(() -> new DataStorageException("Recipe not found, id: " + entity.getId()));
        var updatedRecipe = recipeMapper.mapExistingEntityToDatabase(entity, existingRecipe.getId());

        recipeDao.update(updatedRecipe);
    }

    @Override
    public void deleteByGuid(String id) {
        recipeDao.deleteByGuid(id);
    }

    @Override
    public void deleteAll() {
        recipeDao.deleteAll();
    }

    @Override
    public boolean existsByGuid(String id) {
        return recipeDao.existsByGuid(id);
    }

    @Override
    public Optional<Recipe> findByGuid(String id) {
        return recipeDao
                .findByGuid(id)
                .map(recipeMapper::mapToBusiness);
    }
}
