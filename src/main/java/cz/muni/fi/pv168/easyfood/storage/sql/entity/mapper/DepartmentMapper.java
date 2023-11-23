package cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.DepartmentEntity;

/**
 * Mapper from the {@link DepartmentEntity} to {@link Department}.
 */
public final class DepartmentMapper implements EntityMapper<DepartmentEntity, Department> {

    @Override
    public Department mapToBusiness(DepartmentEntity dbDepartment) {
        return new Department(
                dbDepartment.guid(),
                dbDepartment.name(),
                dbDepartment.number()
        );
    }

    @Override
    public DepartmentEntity mapNewEntityToDatabase(Department entity) {
        return getDepartmentEntity(entity, null);
    }

    @Override
    public DepartmentEntity mapExistingEntityToDatabase(Department entity, Long dbId) {
        return getDepartmentEntity(entity, dbId);
    }

    private static DepartmentEntity getDepartmentEntity(Department entity, Long dbId) {
        return new DepartmentEntity(
                dbId,
                entity.getGuid(),
                entity.getNumber(),
                entity.getName()
        );
    }
}
