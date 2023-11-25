package cz.muni.fi.pv168.easyfood.business.service.validation;

import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.service.validation.common.StringLengthValidator;

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
