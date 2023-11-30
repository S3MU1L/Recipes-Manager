package cz.muni.fi.pv168.easyfood.business.service.validation;

import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.service.validation.common.StringLengthValidator;

import java.util.List;

public class IngredientValidator implements Validator<Ingredient> {
    @Override
    public ValidationResult validate(Ingredient ingredient) {
        var validators = List.of(
                Validator.extracting(Ingredient::getName, new StringLengthValidator(1, 150, "Ingredient name"))
        );
        return Validator.compose(validators).validate(ingredient);
    }

}
