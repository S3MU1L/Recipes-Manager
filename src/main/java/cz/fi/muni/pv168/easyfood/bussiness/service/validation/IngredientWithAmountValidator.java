package cz.fi.muni.pv168.easyfood.bussiness.service.validation;

import cz.fi.muni.pv168.easyfood.bussiness.model.IngredientWithAmount;
import cz.fi.muni.pv168.easyfood.bussiness.service.validation.common.StringLengthValidator;

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
