package cz.fi.muni.pv168.easyfood.bussiness.service.crud;

import cz.fi.muni.pv168.easyfood.bussiness.model.GuidProvider;
import cz.fi.muni.pv168.easyfood.bussiness.repository.Repository;
import cz.fi.muni.pv168.easyfood.bussiness.service.validation.ValidationResult;
import cz.fi.muni.pv168.easyfood.bussiness.service.validation.Validator;
import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;

import java.util.List;

public class IngredientCrudService implements CrudService<Ingredient> {
    private final Repository<Ingredient> ingredientRepository;
    private final Validator<Ingredient> ingredientValidator;
    private final GuidProvider guidProvider;

    public IngredientCrudService(Repository<Ingredient> ingredientRepository,
                                 Validator<Ingredient> ingredientValidator,
                                 GuidProvider guidProvider) {
        this.ingredientRepository = ingredientRepository;
        this.ingredientValidator = ingredientValidator;
        this.guidProvider = guidProvider;
    }

    @Override
    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    @Override
    public ValidationResult create(Ingredient newIngredient) {
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
    public ValidationResult update(Ingredient entity) {
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
