package cz.muni.fi.pv168.easyfood.business.service.validation;

import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.service.validation.common.StringLengthValidator;

import java.time.Clock;
import java.util.List;

import static cz.muni.fi.pv168.easyfood.business.service.validation.Validator.extracting;

/**
 * Employee entity {@link Validator}.
 */
public class EmployeeValidator implements Validator<Employee> {

    @Override
    public ValidationResult validate(Employee model) {
        var validators = List.of(
                extracting(Employee::getFirstName, new StringLengthValidator(2, 150, "First name")),
                extracting(Employee::getLastName, new StringLengthValidator(2, 150, "Last name")),
                new EmployeeAgeValidator(Clock.systemUTC())
        );

        return Validator.compose(validators).validate(model);
    }
}
