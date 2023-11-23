package cz.muni.fi.pv168.easyfood.business.service.validation;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.model.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link EmployeeAgeValidator}.
 */
class EmployeeAgeValidatorTest {

    // Using this, we simulate that the current date is 1.1.2018
    private static final Clock EPOCH_CLOCK = Clock.fixed(LocalDate.of(2018, 1, 1)
            .atStartOfDay()
            .toInstant(ZoneOffset.UTC), ZoneOffset.UTC);

    private EmployeeAgeValidator employeeAgeValidator;

    @BeforeEach
    void setUp() {
        employeeAgeValidator = new EmployeeAgeValidator(EPOCH_CLOCK);
    }

    @Test
    void validateSuccess() {
        var result = employeeAgeValidator.validate(setUpEmployee(LocalDate.of(2000, 1, 1)));

        assertThat(result).isEqualTo(ValidationResult.success());
    }

    @Test
    void validateFails() {
        var result = employeeAgeValidator.validate(setUpEmployee(LocalDate.of(2001, 1, 1)));

        assertThat(result.isValid()).isFalse();
        assertThat(result.getValidationErrors()).hasSize(1);
        assertThat(result.getValidationErrors().get(0)).isEqualTo("Employee is too young");
    }

    private Employee setUpEmployee(LocalDate birthDate) {
        return new Employee(
                "John",
                "Doe",
                Gender.FEMALE,
                birthDate,
                new Department("IT", "Information Technology", "69")
        );
    }
}