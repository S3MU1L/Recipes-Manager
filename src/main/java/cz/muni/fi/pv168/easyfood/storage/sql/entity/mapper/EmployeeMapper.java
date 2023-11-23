package cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.DepartmentEntity;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.EmployeeEntity;

/**
 * Mapper from the {@link EmployeeEntity} to {@link Employee}.
 */
public class EmployeeMapper implements EntityMapper<EmployeeEntity, Employee> {

    private final DataAccessObject<DepartmentEntity> departmentDao;
    private final EntityMapper<DepartmentEntity, Department> departmentMapper;

    public EmployeeMapper(
            DataAccessObject<DepartmentEntity> departmentDao,
            EntityMapper<DepartmentEntity, Department> departmentMapper) {
        this.departmentDao = departmentDao;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public Employee mapToBusiness(EmployeeEntity entity) {
        var department = departmentDao
                .findById(entity.departmentId())
                .map(departmentMapper::mapToBusiness)
                .orElseThrow(() -> new DataStorageException("Department not found, id: " +
                        entity.departmentId()));

        return new Employee(
                entity.guid(),
                entity.firstName(),
                entity.lastName(),
                entity.gender(),
                entity.birthDate(),
                department
        );
    }

    @Override
    public EmployeeEntity mapNewEntityToDatabase(Employee entity) {
        var departmentEntity = departmentDao
                .findByGuid(entity.getDepartment().getGuid())
                .orElseThrow(() -> new DataStorageException("Department not found, guid: " +
                        entity.getDepartment().getGuid()));

        return new EmployeeEntity(
                entity.getGuid(),
                departmentEntity.id(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getGender(),
                entity.getBirthDate()
        );
    }

    @Override
    public EmployeeEntity mapExistingEntityToDatabase(Employee entity, Long dbId) {
        var departmentEntity = departmentDao
                .findByGuid(entity.getDepartment().getGuid())
                .orElseThrow(() -> new DataStorageException("Department not found, guid: " +
                        entity.getDepartment().getGuid()));

        return new EmployeeEntity(
                dbId,
                entity.getGuid(),
                departmentEntity.id(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getGender(),
                entity.getBirthDate()
        );
    }
}
