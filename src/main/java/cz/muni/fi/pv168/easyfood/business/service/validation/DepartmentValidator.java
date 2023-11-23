package cz.muni.fi.pv168.easyfood.business.service.validation;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.service.validation.common.StringLengthValidator;

import java.util.List;

import static cz.muni.fi.pv168.easyfood.business.service.validation.Validator.extracting;

/**
 * Department entity {@link Validator}
 */
public class DepartmentValidator implements Validator<Department> {
    @Override
    public ValidationResult validate(Department department) {
        var validators = List.of(
                extracting(
                        Department::getName, new StringLengthValidator(1, 50, "Department name")),
                extracting(
                        Department::getNumber, new StringLengthValidator(2, 10, "Department number"))
        );

        return Validator.compose(validators).validate(department);
    }
}
