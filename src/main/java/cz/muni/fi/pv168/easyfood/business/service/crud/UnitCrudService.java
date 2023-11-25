package cz.muni.fi.pv168.easyfood.business.service.crud;

import cz.muni.fi.pv168.easyfood.business.model.GuidProvider;
import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.easyfood.business.service.validation.Validator;

import java.util.List;

public class UnitCrudService implements CrudService<Unit> {
    private final Repository<Unit> unitRepository;
    private final Validator<Unit> unitValidator;
    private final GuidProvider guidProvider;

    public UnitCrudService(Repository<Unit> ingredientRepository,
                           Validator<Unit> ingredientValidator,
                           GuidProvider guidProvider) {
        this.unitRepository = ingredientRepository;
        this.unitValidator = ingredientValidator;
        this.guidProvider = guidProvider;
    }


    @Override
    public List<Unit> findAll() {
        return unitRepository.findAll();
    }

    @Override
    public ValidationResult create(Unit newUnit) {
        var validationResult = unitValidator.validate(newUnit);
        if (newUnit.getGuid() == null || newUnit.getGuid().isBlank()) {
            newUnit.setGuid(guidProvider.newGuid());
        } else if (unitRepository.existsByGuid(newUnit.getGuid())) {
            throw new EntityAlreadyExistsException("Employee with given guid already exists: " + newUnit.getGuid());
        }
        if (validationResult.isValid()) {
            unitRepository.create(newUnit);
        }

        return validationResult;
    }

    @Override
    public ValidationResult update(Unit entity) {
        var validationResult = unitValidator.validate(entity);
        if (validationResult.isValid()) {
            unitRepository.update(entity);
        }

        return validationResult;
    }

    @Override
    public void deleteByGuid(String guid) {
        unitRepository.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        unitRepository.deleteAll();
    }
}
