package cz.muni.fi.pv168.easyfood.business.service.crud;

import cz.muni.fi.pv168.easyfood.business.model.GuidProvider;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.easyfood.business.service.validation.Validator;
import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;

import java.util.List;

public class IngredientWithAmountCrudService implements CrudService<IngredientWithAmount> {
    private final Repository<IngredientWithAmount> ingredientRepository;
    private final Validator<IngredientWithAmount> ingredientValidator;
    private final GuidProvider guidProvider;

    public IngredientWithAmountCrudService(Repository<IngredientWithAmount> ingredientRepository,
                                           Validator<IngredientWithAmount> ingredientValidator,
                                           GuidProvider guidProvider) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientValidator = ingredientValidator;
        this.guidProvider = guidProvider;
    }

    @Override
    public List<IngredientWithAmount> findAll() {
        return ingredientRepository.findAll();
    }

    @Override
    public ValidationResult create(IngredientWithAmount newIngredient) {
        var validationResult = ingredientValidator.validate(newIngredient);
        if (newIngredient.getGuid() == null || newIngredient.getGuid().isBlank()) {
            newIngredient.setGuid(guidProvider.newGuid());
        } else if (ingredientRepository.existsByGuid(newIngredient.getGuid())) {
            throw new EntityAlreadyExistsException("Employee with given guid already exists: " + newIngredient.getGuid());
        }
        if (validationResult.isValid()) {
            ingredientRepository.create(newIngredient);
        }

        return validationResult;
    }

    @Override
    public ValidationResult update(IngredientWithAmount entity) {
        var validationResult = ingredientValidator.validate(entity);
        if (validationResult.isValid()) {
            ingredientRepository.update(entity);
        }

        return validationResult;
    }

    @Override
    public void deleteByGuid(String guid) {
        ingredientRepository.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        ingredientRepository.deleteAll();
    }
}
