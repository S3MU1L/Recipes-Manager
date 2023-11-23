package cz.muni.fi.pv168.easyfood.business.service.crud;

import cz.muni.fi.pv168.easyfood.business.error.RuntimeApplicationException;

import java.io.Serial;

/**
 * Thrown, if there is already an existing entity.
 *
 * @author Vojtech Sassmann
 * @since 1.0.0
 */
public class EntityAlreadyExistsException extends RuntimeApplicationException {

    @Serial
    private static final long serialVersionUID = 0L;

    public EntityAlreadyExistsException(String message)
    {
        super(message);
    }
}
