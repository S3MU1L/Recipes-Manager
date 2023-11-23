package cz.muni.fi.pv168.easyfood.business.service.crud;

import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.model.GuidProvider;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.easyfood.business.service.validation.Validator;
import org.tinylog.Logger;

import java.util.List;

/**
 * Crud operations for the {@link Employee} entity.
 */
public final class EmployeeCrudService implements CrudService<Employee> {

    private final Repository<Employee> employeeRepository;
    private final Validator<Employee> employeeValidator;
    private final GuidProvider guidProvider;

    public EmployeeCrudService(Repository<Employee> employeeRepository, Validator<Employee> employeeValidator,
                               GuidProvider guidProvider) {
        this.employeeRepository = employeeRepository;
        this.employeeValidator = employeeValidator;
        this.guidProvider = guidProvider;
    }

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public ValidationResult create(Employee newEntity) {
        var validationResult = employeeValidator.validate(newEntity);
        if (newEntity.getGuid() == null || newEntity.getGuid().isBlank()) {
            newEntity.setGuid(guidProvider.newGuid());
        } else if (employeeRepository.existsByGuid(newEntity.getGuid())) {
            throw new EntityAlreadyExistsException("Employee with given guid already exists: " + newEntity.getGuid());
        }
        if (validationResult.isValid()) {
            employeeRepository.create(newEntity);

            Logger.info("Created new employee: {}", newEntity);
        }

        return validationResult;
    }

    @Override
    public ValidationResult update(Employee entity) {
        var validationResult = employeeValidator.validate(entity);
        if (validationResult.isValid()) {
            employeeRepository.update(entity);

            Logger.info("Updated employee: {}", entity);
        }

        return validationResult;
    }

    @Override
    public void deleteByGuid(String guid) {
        employeeRepository.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        employeeRepository.deleteAll();
    }
}
