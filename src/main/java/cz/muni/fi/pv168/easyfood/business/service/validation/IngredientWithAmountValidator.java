package cz.muni.fi.pv168.easyfood.business.service.validation;

import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;
import cz.muni.fi.pv168.easyfood.business.service.validation.common.StringLengthValidator;

import java.util.List;

public class IngredientWithAmountValidator implements Validator<IngredientWithAmount> {
    @Override
    public ValidationResult validate(IngredientWithAmount model) {
        var validators = List.of(
                Validator.extracting(IngredientWithAmount::getName, new StringLengthValidator(1, 150, "Ingredient name"))
        );

        return Validator.compose(validators).validate(model);
    }
}
