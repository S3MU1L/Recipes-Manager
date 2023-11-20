package cz.fi.muni.pv168.easyfood.storage.sql.db;

import java.io.Closeable;

/**
 * Transaction Handling
 */
public interface Transaction extends Closeable {

    /**
     * @return active {@link ConnectionHandler} instance
     */
    ConnectionHandler connection();

    /**
     * Commits active transaction
     */
    void commit();

    /**
     * Closes active connection
     */
    void close();

    /**
     * Returns true if connection is closed
     */
    boolean isClosed();
}
