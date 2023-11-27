package cz.muni.fi.pv168.easyfood.business.service.validation;


import cz.muni.fi.pv168.easyfood.business.error.RuntimeApplicationException;

import java.util.Collections;
import java.util.List;

public class ValidationException extends RuntimeApplicationException {
    private final List<String> validationErrors;

    public ValidationException(String message, List<String> validationErrors) {
        super("Validation failed: " + message);
        this.validationErrors = validationErrors;
    }

    public List<String> getValidationErrors() {
        return Collections.unmodifiableList(validationErrors);
    }
}
