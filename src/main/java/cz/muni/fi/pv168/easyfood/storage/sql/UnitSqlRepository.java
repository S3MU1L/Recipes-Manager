package cz.muni.fi.pv168.easyfood.storage.sql;

import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.storage.DataStorageException;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.UnitEntity;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper.EntityMapper;

import java.util.List;
import java.util.Optional;

public class UnitSqlRepository implements Repository<Unit> {

    private final DataAccessObject<UnitEntity> unitDao;
    private final EntityMapper<UnitEntity, Unit> unitMapper;

    public UnitSqlRepository(
            DataAccessObject<UnitEntity> unitDao,
            EntityMapper<UnitEntity, Unit> categoryMapper) {
        this.unitDao = unitDao;
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
        var existingUnit = unitDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Unit not found, id: " + entity.getGuid()));
        var updatedUnit = unitMapper.mapExistingEntityToDatabase(entity, existingUnit.id());

        unitDao.update(updatedUnit);
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
