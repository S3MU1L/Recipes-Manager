package cz.muni.fi.pv168.easyfood.business.service.validation;

import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.service.validation.common.StringLengthValidator;

import java.util.List;

public class RecipeValidator implements Validator<Recipe> {
    @Override
    public ValidationResult validate(Recipe model) {
        var validators = List.of(
                Validator.extracting(Recipe::getName, new StringLengthValidator(1, 150, "Recipe name"))
        );
        return Validator.compose(validators).validate(model);
    }
}
