package cz.fi.muni.pv168.easyfood.bussiness.service.export;


import cz.fi.muni.pv168.easyfood.bussiness.error.RuntimeApplicationException;

/**
 * Exception thrown if there is some problem with data import/export.
 */
public final class DataManipulationException extends RuntimeApplicationException {

    public DataManipulationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataManipulationException(String message) {
        super(message);
    }
}
