package cz.fi.muni.pv168.easyfood.data;

import java.util.List;

/**
 * Generic interface for CRUD operations on entities
 *
 * @param <T> type of the entity this DAO operates on
 */
public interface DataAccessObject<T> {

    /**
     * Creates a new entity using the underlying data source
     * @param entity entity to be persisted
     * @throws IllegalArgumentException when the entity has already been persisted
     * @throws DataAccessException when anything goes wrong with the underlying data source
     */
    Void create(T entity);

    /**
     * Reads all entities from the underlying data source
     * @return list of all entities known to the underlying data source
     * @throws DataAccessException when anything goes wrong with the underlying data source
     */
    List<T> findAll();

    /**
     * Updates an entity using the underlying data source
     * @param entity entity to be deleted
     * @throws IllegalArgumentException when the entity has not been persisted yet
     * @throws DataAccessException when anything goes wrong with the underlying data source
     * @return
     */
    Void update(T entity);

    /**
     * Deletes an entity using the underlying data source
     * @param entity entity to be deleted
     * @throws IllegalArgumentException when the entity has not been persisted yet
     * @throws DataAccessException when anything goes wrong with the underlying data source
     * @return
     */
    Void delete(T entity);
}
