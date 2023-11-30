package cz.muni.fi.pv168.easyfood.business.service.export.batch;


import cz.muni.fi.pv168.easyfood.business.error.RuntimeApplicationException;

/**
 * Exception thrown in case there is a problem with a bulk operation.
 */
public class BatchOperationException extends RuntimeApplicationException {
    public BatchOperationException(String message) {
        super(message);
    }

    public BatchOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
