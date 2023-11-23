package cz.muni.fi.pv168.easyfood.storage.sql;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.DepartmentEntity;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper.EntityMapper;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link Repository} for {@link Department} entity using SQL database.
 *
 * @author Vojtech Sassmann
 */
public class DepartmentSqlRepository implements Repository<Department> {

    private final DataAccessObject<DepartmentEntity> departmentDao;
    private final EntityMapper<DepartmentEntity, Department> departmentMapper;

    public DepartmentSqlRepository(
            DataAccessObject<DepartmentEntity> departmentDao, 
            EntityMapper<DepartmentEntity, Department> departmentMapper) {
        this.departmentDao = departmentDao;
        this.departmentMapper = departmentMapper;
    }

    @Override
    public List<Department> findAll() {
        return departmentDao
                .findAll()
                .stream()
                .map(departmentMapper::mapToBusiness)
                .toList();
    }

    @Override
    public void create(Department newEntity) {
        departmentDao.create(departmentMapper.mapNewEntityToDatabase(newEntity));
    }

    @Override
    public void update(Department entity) {
        var existingDepartment = departmentDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Department not found, guid: " + entity.getGuid()));
        var updatedDepartment = departmentMapper.mapExistingEntityToDatabase(entity, existingDepartment.id());

        departmentDao.update(updatedDepartment);
    }

    @Override
    public void deleteByGuid(String guid) {
        departmentDao.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        departmentDao.deleteAll();
    }

    @Override
    public boolean existsByGuid(String guid) {
        return departmentDao.existsByGuid(guid);
    }

    @Override
    public Optional<Department> findByGuid(String guid) {
        return departmentDao
                .findByGuid(guid)
                .map(departmentMapper::mapToBusiness);
    }
}
