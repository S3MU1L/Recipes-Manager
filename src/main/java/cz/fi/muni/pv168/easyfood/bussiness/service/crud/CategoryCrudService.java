package cz.fi.muni.pv168.easyfood.bussiness.service.crud;

import cz.fi.muni.pv168.easyfood.bussiness.model.GuidProvider;
import cz.fi.muni.pv168.easyfood.bussiness.repository.Repository;
import cz.fi.muni.pv168.easyfood.bussiness.service.validation.ValidationResult;
import cz.fi.muni.pv168.easyfood.bussiness.service.validation.Validator;
import cz.fi.muni.pv168.easyfood.bussiness.model.Category;

import java.util.List;

public class CategoryCrudService implements CrudService<Category> {
    private final Repository<Category> categoryRepository;
    private final Validator<Category> categoryValidator;
    private final GuidProvider guidProvider;

    public CategoryCrudService(Repository<Category> ingredientRepository,
                               Validator<Category> ingredientValidator,
                               GuidProvider guidProvider) {
        this.categoryRepository = ingredientRepository;
        this.categoryValidator = ingredientValidator;
        this.guidProvider = guidProvider;
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public ValidationResult create(Category newCategory) {
        var validationResult = categoryValidator.validate(newCategory);
        if (newCategory.getGuid() == null || newCategory.getGuid().isBlank()) {
            newCategory.setGuid(guidProvider.newGuid());
        } else if (categoryRepository.existsByGuid(newCategory.getGuid())) {
            throw new EntityAlreadyExistsException("Employee with given guid already exists: " + newCategory.getGuid());
        }
        if (validationResult.isValid()) {
            categoryRepository.create(newCategory);
        }

        return validationResult;
    }

    @Override
    public ValidationResult update(Category entity) {
        var validationResult = categoryValidator.validate(entity);
        if (validationResult.isValid()) {
            categoryRepository.update(entity);
        }

        return validationResult;
    }

    @Override
    public void deleteByGuid(String guid) {
        categoryRepository.deleteByGuid(guid);
    }

    @Override
    public void deleteAll() {
        categoryRepository.deleteAll();
    }
}
