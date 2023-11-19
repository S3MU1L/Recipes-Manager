package cz.fi.muni.pv168.easyfood.storage.sql.dao;

import cz.fi.muni.pv168.easyfood.storage.sql.db.ConnectionHandler;
import cz.fi.muni.pv168.easyfood.storage.sql.entity.RecipeEntity;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

public class RecipeDao implements DataAccessObject<RecipeEntity> {
    private final Supplier<ConnectionHandler> connections;

    public RecipeDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    @Override
    public RecipeEntity create(RecipeEntity entity) {
        return null;
    }

    @Override
    public Collection<RecipeEntity> findAll() {
        return null;
    }

    @Override
    public Optional<RecipeEntity> findById(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<RecipeEntity> findByGuid(String guid) {
        return Optional.empty();
    }

    @Override
    public RecipeEntity update(RecipeEntity entity) {
        return null;
    }

    @Override
    public void deleteByGuid(String guid) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public boolean existsByGuid(String guid) {
        return false;
    }
}
