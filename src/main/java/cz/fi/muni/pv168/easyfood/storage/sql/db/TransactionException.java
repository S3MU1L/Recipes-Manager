package cz.fi.muni.pv168.easyfood.storage.sql.db;


import cz.fi.muni.pv168.easyfood.storage.sql.dao.DataStorageException;

/**
 * Thrown if some transaction operation fails.
 */
public class TransactionException extends DataStorageException {

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
