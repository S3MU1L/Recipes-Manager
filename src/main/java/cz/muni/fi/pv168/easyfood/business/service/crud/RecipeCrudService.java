package cz.muni.fi.pv168.easyfood.business.service.crud;

import cz.muni.fi.pv168.easyfood.business.model.GuidProvider;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.business.service.validation.ValidationResult;
import cz.muni.fi.pv168.easyfood.business.service.validation.Validator;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;

import java.util.List;

public class RecipeCrudService implements CrudService<Recipe> {

    private final Repository<Recipe> recipeRepository;
    private final Validator<Recipe> recipeValidator;
    private final GuidProvider guidProvider;

    public RecipeCrudService(Repository<Recipe> recipeRepository,
                             Validator<Recipe> recipeValidator,
                             GuidProvider guidProvider) {
        this.recipeRepository = recipeRepository;
        this.recipeValidator = recipeValidator;
        this.guidProvider = guidProvider;
    }

    @Override
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    @Override
    public ValidationResult create(Recipe newRecipe) {
        var validationResult = recipeValidator.validate(newRecipe);
        if (newRecipe.getGuid() == null || newRecipe.getGuid().isBlank()) {
            newRecipe.setGuid(guidProvider.newGuid());
        } else if (recipeRepository.existsByGuid(newRecipe.getGuid())) {
            throw new EntityAlreadyExistsException("Employee with given guid already exists: " + newRecipe.getGuid());
        }
        if (validationResult.isValid()) {
            recipeRepository.create(newRecipe);
        }

        return validationResult;
    }

    @Override
    public ValidationResult update(Recipe entity) {
        var validationResult = recipeValidator.validate(entity);
        if (validationResult.isValid()) {
            recipeRepository.update(entity);
        }

        return validationResult;
    }

    @Override
    public void deleteByGuid(String guid) {
        recipeRepository.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        recipeRepository.deleteAll();
    }
}
