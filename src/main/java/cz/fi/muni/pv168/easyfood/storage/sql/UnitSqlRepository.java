package cz.fi.muni.pv168.easyfood.storage.sql;

import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;
import cz.fi.muni.pv168.easyfood.bussiness.repository.Repository;
import cz.fi.muni.pv168.easyfood.storage.DataStorageException;
import cz.fi.muni.pv168.easyfood.storage.sql.dao.DataAccessObject;
import cz.fi.muni.pv168.easyfood.storage.sql.entity.UnitEntity;
import cz.fi.muni.pv168.easyfood.storage.sql.entity.mapper.EntityMapper;

import java.util.List;
import java.util.Optional;

public class UnitSqlRepository implements Repository<Unit> {

    private final DataAccessObject<UnitEntity> unitDao;
    private final EntityMapper<UnitEntity, Unit> unitMapper;

    public UnitSqlRepository(
            DataAccessObject<UnitEntity> recipeDao,
            EntityMapper<UnitEntity, Unit> categoryMapper) {
        this.unitDao = recipeDao;
        this.unitMapper = categoryMapper;
    }

    @Override
    public List<Unit> findAll() {
        return unitDao
                .findAll()
                .stream()
                .map(unitMapper::mapToBusiness)
                .toList();
    }

    @Override
    public void create(Unit newEntity) {
        unitDao.create(unitMapper.mapNewEntityToDatabase(newEntity));
    }

    @Override
    public void update(Unit entity) {
        var existingRecipe = unitDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Recipe not found, id: " + entity.getGuid()));
        var updatedRecipe = unitMapper.mapExistingEntityToDatabase(entity, existingRecipe.id());

        unitDao.update(updatedRecipe);
    }

    @Override
    public void deleteByGuid(String id) {
        unitDao.deleteByGuid(id);
    }

    @Override
    public void deleteAll() {
        unitDao.deleteAll();
    }

    @Override
    public boolean existsByGuid(String id) {
        return unitDao.existsByGuid(id);
    }

    @Override
    public Optional<Unit> findByGuid(String id) {
        return unitDao
                .findByGuid(id)
                .map(unitMapper::mapToBusiness);
    }
}
