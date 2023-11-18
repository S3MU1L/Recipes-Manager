package cz.fi.muni.pv168.easyfood.bussiness.service.crud;


import cz.fi.muni.pv168.easyfood.bussiness.error.RuntimeApplicationException;

import java.io.Serial;

/**
 * Thrown, if there is already an existing entity.
 */
public class EntityAlreadyExistsException extends RuntimeApplicationException {

    @Serial
    private static final long serialVersionUID = 0L;

    public EntityAlreadyExistsException(String message)
    {
        super(message);
    }
}
