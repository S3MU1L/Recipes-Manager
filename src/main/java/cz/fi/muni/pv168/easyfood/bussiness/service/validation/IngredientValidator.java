package cz.fi.muni.pv168.easyfood.bussiness.service.validation;

import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.service.validation.common.StringLengthValidator;

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
