package cz.fi.muni.pv168.easyfood.storage.sql.dao;


import cz.fi.muni.pv168.easyfood.bussiness.error.FatalError;

import java.io.Serial;

public class FatalDataStorageException extends DataStorageException implements FatalError {

    @Serial
    private static final long serialVersionUID = -7034897190745766939L;

    public FatalDataStorageException(String message) {
        super(message);
    }

    public FatalDataStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public FatalDataStorageException(String userMessage, String message, Throwable cause) {
        super(userMessage, message, cause);
    }
}

