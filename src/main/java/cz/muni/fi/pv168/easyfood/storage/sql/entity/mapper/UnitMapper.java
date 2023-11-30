package cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper;

import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.UnitEntity;

public class UnitMapper implements EntityMapper<UnitEntity, Unit> {
    @Override
    public Unit mapToBusiness(UnitEntity entity) {
        return new Unit(
                entity.guid(),
                entity.name(),
                entity.abbreviation(),
                entity.baseUnit(),
                entity.conversion()
        );
    }

    @Override
    public UnitEntity mapNewEntityToDatabase(Unit entity) {
        return getUnitEntity(entity, null);
    }

    @Override
    public UnitEntity mapExistingEntityToDatabase(Unit entity, Long dbId) {
        return getUnitEntity(entity, dbId);
    }

    private static UnitEntity getUnitEntity(Unit entity, Long dbId) {
        return new UnitEntity(
                dbId,
                entity.getGuid(),
                entity.getBaseUnit(),
                entity.getName(),
                entity.getAbbreviation(),
                entity.getConversion()
        );
    }
}
