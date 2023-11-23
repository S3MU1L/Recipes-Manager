package cz.fi.muni.pv168.easyfood.bussiness.service.crud;

import cz.fi.muni.pv168.easyfood.bussiness.model.GuidProvider;
import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;
import cz.fi.muni.pv168.easyfood.bussiness.repository.Repository;
import cz.fi.muni.pv168.easyfood.bussiness.service.validation.ValidationResult;
import cz.fi.muni.pv168.easyfood.bussiness.service.validation.Validator;

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
