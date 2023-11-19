package cz.fi.muni.pv168.easyfood.storage.sql.db;

import java.io.Closeable;
import java.sql.Connection;

/**
 * Handler useful for greater control over {@link Connection}
 */
public interface ConnectionHandler extends Closeable {

    /**
     * @return {@link Connection} instance managed by this handler
     */
    Connection use();

    /**
     * Closes managed {@link Connection} when desired
     */
    void close();
}
