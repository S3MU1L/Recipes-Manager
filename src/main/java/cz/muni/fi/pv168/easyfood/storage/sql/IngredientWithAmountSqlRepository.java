package cz.muni.fi.pv168.easyfood.storage.sql;

import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.storage.DataStorageException;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.IngredientWithAmountEntity;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper.EntityMapper;

import java.util.List;
import java.util.Optional;

public class IngredientWithAmountSqlRepository implements Repository<IngredientWithAmount> {

    private final DataAccessObject<IngredientWithAmountEntity> ingredientWithAmountDao;
    private final EntityMapper<IngredientWithAmountEntity, IngredientWithAmount> ingredientWithAmountMapper;

    public IngredientWithAmountSqlRepository(
            DataAccessObject<IngredientWithAmountEntity> ingredientWithAmountDao,
            EntityMapper<IngredientWithAmountEntity, IngredientWithAmount> ingredientWithAmountMapper) {
        this.ingredientWithAmountDao = ingredientWithAmountDao;
        this.ingredientWithAmountMapper = ingredientWithAmountMapper;
    }

    @Override
    public List<IngredientWithAmount> findAll() {
        return ingredientWithAmountDao
                .findAll()
                .stream()
                .map(ingredientWithAmountMapper::mapToBusiness)
                .toList();
    }

    @Override
    public void create(IngredientWithAmount newEntity) {
        ingredientWithAmountDao.create(ingredientWithAmountMapper.mapNewEntityToDatabase(newEntity));
    }

    @Override
    public void update(IngredientWithAmount entity) {
        var existingIngredientWithAmount = ingredientWithAmountDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("IngredientWithAmount not found, id: " + entity.getGuid()));
        var updatedIngredientWithAmount = ingredientWithAmountMapper.mapExistingEntityToDatabase(entity, existingIngredientWithAmount.id());

        ingredientWithAmountDao.update(updatedIngredientWithAmount);
    }

    @Override
    public void deleteByGuid(String guid) {
        ingredientWithAmountDao.deleteByGuid(guid);
    }

    @Override
    public Optional<IngredientWithAmount> findByGuid(String guid) {
        return ingredientWithAmountDao
                .findByGuid(guid)
                .map(ingredientWithAmountMapper::mapToBusiness);
    }

    @Override
    public Optional<IngredientWithAmount> findByName(String name) {
        return ingredientWithAmountDao
                .findByName(name)
                .map(ingredientWithAmountMapper::mapToBusiness);
    }

    @Override
    public void deleteAll() {
        ingredientWithAmountDao.deleteAll();
    }

    @Override
    public boolean existsByGuid(String guid) {
        return ingredientWithAmountDao.existsByGuid(guid);
    }
}
