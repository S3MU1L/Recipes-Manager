package cz.muni.fi.pv168.easyfood.storage.sql;

import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.DataAccessObject;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.DataStorageException;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.EmployeeEntity;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper.EntityMapper;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link Repository} for {@link Employee} entity using SQL database.
 *
 * @author Vojtech Sassmann
 */
public class EmployeeSqlRepository implements Repository<Employee> {

    private final DataAccessObject<EmployeeEntity> employeeDao;
    private final EntityMapper<EmployeeEntity, Employee> employeeMapper;

    public EmployeeSqlRepository(
            DataAccessObject<EmployeeEntity> employeeDao,
            EntityMapper<EmployeeEntity, Employee> employeeMapper) {
        this.employeeDao = employeeDao;
        this.employeeMapper = employeeMapper;
    }


    @Override
    public List<Employee> findAll() {
        return employeeDao
                .findAll()
                .stream()
                .map(employeeMapper::mapToBusiness)
                .toList();
    }

    @Override
    public void create(Employee newEntity) {
        employeeDao.create(employeeMapper.mapNewEntityToDatabase(newEntity));
    }

    @Override
    public void update(Employee entity) {
        var existingEmployee = employeeDao.findByGuid(entity.getGuid())
                .orElseThrow(() -> new DataStorageException("Employee not found, guid: " + entity.getGuid()));
        var updatedEmployeeEntity = employeeMapper
                .mapExistingEntityToDatabase(entity, existingEmployee.id());

        employeeDao.update(updatedEmployeeEntity);
    }

    @Override
    public void deleteByGuid(String guid) {
        employeeDao.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        employeeDao.deleteAll();
    }

    @Override
    public boolean existsByGuid(String guid) {
        return employeeDao.existsByGuid(guid);
    }

    @Override
    public Optional<Employee> findByGuid(String guid) {
       return employeeDao
            .findByGuid(guid)
            .map(employeeMapper::mapToBusiness);
    }
}
