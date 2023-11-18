package cz.fi.muni.pv168.easyfood.bussiness.service.validation;

import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.service.validation.common.StringLengthValidator;

import java.util.List;

public class CategoryValidator implements Validator<Category> {
    @Override
    public ValidationResult validate(Category model) {
        var validators = List.of(
                Validator.extracting(Category::getName, new StringLengthValidator(2, 150, "First name"))
        );

        return Validator.compose(validators).validate(model);
    }
}
