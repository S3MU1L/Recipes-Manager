package cz.muni.fi.pv168.easyfood.business.service.validation.common;

import cz.muni.fi.pv168.easyfood.business.service.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link StringLengthValidator}
 */
class StringLengthValidatorTest {

    private StringLengthValidator stringLengthValidator;

    @BeforeEach
    void setUp() {
        stringLengthValidator = new StringLengthValidator(2, 10);
    }

    @Test
    void validateSuccess() {
        var result = stringLengthValidator.validate("test");

        assertThat(result).isEqualTo(ValidationResult.success());
    }

    @Test
    void validateLowerBoundFail() {
        var result = stringLengthValidator.validate("t");

        assertThat(result).isEqualTo(ValidationResult
                .failed("'t' length is not between 2 (inclusive) and 10 (inclusive)"));
    }

    @Test
    void validateUpperBoundFail() {
        var result = stringLengthValidator.validate("xxxxxxxxxxx");

        assertThat(result).isEqualTo(ValidationResult
                .failed("'xxxxxxxxxxx' length is not between 2 (inclusive) and 10 (inclusive)"));
    }

    @Test
    void validateLowerBoundSuccess() {
        var result = stringLengthValidator.validate("te");

        assertThat(result).isEqualTo(ValidationResult.success());
    }

    @Test
    void validateUpperBoundSuccess() {
        var result = stringLengthValidator.validate("xxxxxxxxxx");

        assertThat(result).isEqualTo(ValidationResult.success());
    }
}