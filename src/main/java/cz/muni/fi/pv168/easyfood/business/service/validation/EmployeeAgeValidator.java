package cz.muni.fi.pv168.easyfood.business.service.validation;

import cz.muni.fi.pv168.easyfood.business.model.Employee;

import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;

public class EmployeeAgeValidator implements Validator<Employee> {

    private static final int MIN_AGE = 18;
    private final Clock clock;

    public EmployeeAgeValidator(Clock clock) {
        // Using the Clock abstraction, we can easily simulate the current date in tests
        this.clock = clock;
    }

    @Override
    public ValidationResult validate(Employee employee) {
        if (calculateAge(employee) < MIN_AGE) {
            return ValidationResult.failed("Employee is too young");
        }
        return ValidationResult.success();
    }

    private int calculateAge(Employee employee) {
        return Period
                .between(employee.getBirthDate(), LocalDate.now(clock))
                .getYears();
    }
}
