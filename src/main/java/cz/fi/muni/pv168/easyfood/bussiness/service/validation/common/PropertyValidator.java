package cz.fi.muni.pv168.easyfood.bussiness.service.validation.common;


import cz.fi.muni.pv168.easyfood.bussiness.service.validation.Validator;

/**
 * Validator based on Entity property values.
 *
 * @param <T> entity type
 */
public abstract class PropertyValidator<T> implements Validator<T> {

    public final String name;

    protected PropertyValidator(String name) {
        this.name = name;
    }

    protected String getName() {
        return name;
    }

    protected String getName(Object defaultName) {
        return(name != null) ? getName() : String.valueOf(defaultName);
    }
}
