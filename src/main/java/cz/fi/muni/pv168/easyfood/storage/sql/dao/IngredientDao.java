package cz.fi.muni.pv168.easyfood.storage.sql.dao;

import cz.fi.muni.pv168.easyfood.storage.sql.db.ConnectionHandler;
import cz.fi.muni.pv168.easyfood.storage.sql.entity.IngredientEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class IngredientDao implements DataAccessObject<IngredientEntity> {

    private final Supplier<ConnectionHandler> connections;

    public IngredientDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    @Override
    public IngredientEntity create(IngredientEntity entity) {
        var sql = """
                INSERT INTO Ingredient(guid, unitId, name, calories)
                VALUES (?, ?, ?, ?)
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.guid());
            statement.setLong(2, entity.unitId());
            statement.setString(3, entity.name());
            statement.setDouble(4, entity.calories());
            statement.executeUpdate();

            try (var keyResultSet = statement.getGeneratedKeys()) {
                long ingredientId;

                if (keyResultSet.next()) {
                    ingredientId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + entity);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + entity);
                }

                return findById(ingredientId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + entity, ex);
        }
    }

    @Override
    public Collection<IngredientEntity> findAll() {
        var sql = "SELECT id, guid, unitId, name, calories FROM Ingredient";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {

            List<IngredientEntity> ingredients = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var ingredient = ingredientFromResultSet(resultSet);
                    ingredients.add(ingredient);
                }
            }

            return ingredients;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all ingredients", ex);
        }
    }

    private static IngredientEntity ingredientFromResultSet(ResultSet resultSet) throws SQLException {
        return new IngredientEntity(
                resultSet.getLong("id"),
                resultSet.getString("guid"),
                resultSet.getLong("unitId"),
                resultSet.getString("name"),
                resultSet.getDouble("calories")
        );
    }

    @Override
    public Optional<IngredientEntity> findById(long id) {
        var sql = "SELECT id, guid, unitId, name, calories FROM Ingredient WHERE id = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(ingredientFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load ingredient by id", ex);
        }
    }

    @Override
    public Optional<IngredientEntity> findByGuid(String guid) {
        var sql = "SELECT id, guid, unitId, name, calories FROM Ingredient WHERE guid = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(ingredientFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load ingredient by guid", ex);
        }
    }

    @Override
    public IngredientEntity update(IngredientEntity entity) {
        var sql = "UPDATE Ingredient SET unitId = ?, name = ?, calories = ? WHERE id = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, entity.unitId());
            statement.setString(2, entity.name());
            statement.setDouble(3, entity.calories());
            statement.setLong(4, entity.id());
            statement.executeUpdate();
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update ingredient: " + entity, ex);
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        var sql = "DELETE FROM Ingredient WHERE guid = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, guid);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete ingredient by guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM Ingredient";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all ingredients", ex);
        }
    }

    @Override
    public boolean existsByGuid(String guid) {
        var sql = "SELECT id FROM Ingredient WHERE guid = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to check if ingredient exists by guid: " + guid, ex);
        }
    }
}
