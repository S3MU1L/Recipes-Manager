package cz.muni.fi.pv168.easyfood.storage.sql;

import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.storage.DataStorageException;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.CategoryEntity;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper.EntityMapper;

import java.util.List;
import java.util.Optional;

public class CategorySqlRepository implements Repository<Category> {

    private final DataAccessObject<CategoryEntity> categoryDao;
    private final EntityMapper<CategoryEntity, Category> categoryMapper;

    public CategorySqlRepository(
            DataAccessObject<CategoryEntity> recipeDao,
            EntityMapper<CategoryEntity, Category> categoryMapper) {
        this.categoryDao = recipeDao;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<Category> findAll() {
        return categoryDao
                .findAll()
                .stream()
                .map(categoryMapper::mapToBusiness)
                .toList();
    }

    @Override
    public void create(Category newEntity) {
        categoryDao.create(categoryMapper.mapNewEntityToDatabase(newEntity));
    }

    @Override
    public void update(Category entity) {
        var existingRecipe = categoryDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Recipe not found, id: " + entity.getGuid()));
        var updatedRecipe = categoryMapper.mapExistingEntityToDatabase(entity, existingRecipe.id());

        categoryDao.update(updatedRecipe);
    }

    @Override
    public void deleteByGuid(String id) {
        categoryDao.deleteByGuid(id);
    }

    @Override
    public void deleteAll() {
        categoryDao.deleteAll();
    }

    @Override
    public boolean existsByGuid(String id) {
        return categoryDao.existsByGuid(id);
    }

    @Override
    public Optional<Category> findByGuid(String id) {
        return categoryDao
                .findByGuid(id)
                .map(categoryMapper::mapToBusiness);
    }

    @Override
    public Optional<Category> findByName(String name) {
        return categoryDao
                .findByName(name)
                .map(categoryMapper::mapToBusiness);
    }
}
