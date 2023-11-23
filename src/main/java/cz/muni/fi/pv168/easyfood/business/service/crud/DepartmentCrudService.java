package cz.muni.fi.pv168.easyfood.business.service.crud;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.GuidProvider;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.easyfood.business.service.validation.Validator;
import org.tinylog.Logger;

import java.util.List;

/**
 * Crud operations for the {@link Department} entity.
 */
public class DepartmentCrudService implements CrudService<Department> {

    private final Repository<Department> departmentRepository;
    private final Validator<Department> departmentValidator;
    private final GuidProvider guidProvider;

    public DepartmentCrudService(Repository<Department> departmentRepository,
                                 Validator<Department> departmentValidator,
                                 GuidProvider guidProvider) {
        this.departmentRepository = departmentRepository;
        this.departmentValidator = departmentValidator;
        this.guidProvider = guidProvider;
    }

    @Override
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    @Override
    public ValidationResult create(Department newEntity) {
        var validationResult = departmentValidator.validate(newEntity);
        if (newEntity.getGuid() == null || newEntity.getGuid().isBlank()) {
            newEntity.setGuid(guidProvider.newGuid());
        } else if (departmentRepository.existsByGuid(newEntity.getGuid())) {
            throw new EntityAlreadyExistsException("Department with given guid already exists: " + newEntity.getGuid());
        }
        if (validationResult.isValid()) {
            departmentRepository.create(newEntity);

            Logger.info("Created new department: {}", newEntity);
        }

        return validationResult;
    }

    @Override
    public ValidationResult update(Department entity) {
        var validationResult = departmentValidator.validate(entity);
        if (validationResult.isValid()) {
            departmentRepository.update(entity);

            Logger.info("Updated department: {}", entity);
        }

        return validationResult;
    }

    @Override
    public void deleteByGuid(String guid) {
        departmentRepository.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        departmentRepository.deleteAll();
    }
}
