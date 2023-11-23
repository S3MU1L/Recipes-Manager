package cz.fi.muni.pv168.easyfood.bussiness.service.validation;

import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;
import cz.fi.muni.pv168.easyfood.bussiness.service.validation.common.StringLengthValidator;

import java.util.List;

public class UnitValidator implements Validator<Unit> {
    @Override
    public ValidationResult validate(Unit model) {
        var validators = List.of(
                Validator.extracting(Unit::getName, new StringLengthValidator(1, 150, "Unit name"))
        );
        return Validator.compose(validators).validate(model);
    }
}
