package cz.fi.muni.pv168.easyfood.bussiness.service.validation;

import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.bussiness.service.validation.common.StringLengthValidator;

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
