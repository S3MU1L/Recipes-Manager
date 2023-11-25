package cz.fi.muni.pv168.easyfood.storage.sql.dao;

import cz.fi.muni.pv168.easyfood.storage.sql.db.ConnectionHandler;
import cz.fi.muni.pv168.easyfood.storage.sql.entity.IngredientWithAmountEntity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class IngredientWithAmountDao implements DataAccessObject<IngredientWithAmountEntity> {

    private final Supplier<ConnectionHandler> connections;

    public IngredientWithAmountDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    @Override
    public IngredientWithAmountEntity create(IngredientWithAmountEntity entity) {
        var sql = """
                INSERT INTO IngredientWithAmount(guid, ingredientId, amount)
                VALUES (?, ?, ?)
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.guid());
            statement.setLong(2, entity.ingredientId());
            statement.setDouble(3, entity.amount());
            statement.executeUpdate();

            try (var keyResultSet = statement.getGeneratedKeys()) {
                long ingredientWithAmountId;

                if (keyResultSet.next()) {
                    ingredientWithAmountId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + entity);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + entity);
                }

                return findById(ingredientWithAmountId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + entity, ex);
        }
    }

    @Override
    public Collection<IngredientWithAmountEntity> findAll() {
        var sql = "SELECT id, guid, ingredientId, amount FROM IngredientWithAmount";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {

            List<IngredientWithAmountEntity> ingredientsWithAmount = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var ingredientWithAmount = ingredientWithAmountFromResultSet(resultSet);
                    ingredientsWithAmount.add(ingredientWithAmount);
                }
            }

            return ingredientsWithAmount;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all ingredients with amount", ex);
        }
    }

    private static IngredientWithAmountEntity ingredientWithAmountFromResultSet(ResultSet resultSet) throws SQLException {
        return new IngredientWithAmountEntity(
                resultSet.getLong("id"),
                resultSet.getString("guid"),
                resultSet.getLong("ingredientId"),
                resultSet.getDouble("amount")
        );
    }

    @Override
    public Optional<IngredientWithAmountEntity> findById(long id) {
        var sql = "SELECT id, guid, ingredientId, amount FROM IngredientWithAmount WHERE id = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(ingredientWithAmountFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load ingredient with amount by id", ex);
        }
    }

    @Override
    public Optional<IngredientWithAmountEntity> findByGuid(String guid) {
        var sql = "SELECT id, guid, ingredientId, amount FROM IngredientWithAmount WHERE guid = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(ingredientWithAmountFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load ingredient with amount by guid", ex);
        }
    }

    @Override
    public IngredientWithAmountEntity update(IngredientWithAmountEntity entity) {
        var sql = "UPDATE IngredientWithAmount SET ingredientId = ?, amount = ? WHERE id = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, entity.ingredientId());
            statement.setDouble(2, entity.amount());
            statement.setLong(3, entity.id());
            statement.executeUpdate();
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update ingredient with amount: " + entity, ex);
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        var sql = "DELETE FROM IngredientWithAmount WHERE guid = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, guid);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete ingredient with amount by guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM IngredientWithAmount";
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
        var sql = "SELECT id FROM IngredientWithAmount WHERE guid = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to check if ingredient with amount exists: " + guid, ex);
        }
    }
}
