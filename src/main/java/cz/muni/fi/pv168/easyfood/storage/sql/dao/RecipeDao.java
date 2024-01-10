package cz.muni.fi.pv168.easyfood.storage.sql.dao;

import cz.muni.fi.pv168.easyfood.storage.sql.db.ConnectionHandler;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.RecipeEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class RecipeDao implements DataAccessObject<RecipeEntity> {
    private final Supplier<ConnectionHandler> connections;

    public RecipeDao(Supplier<ConnectionHandler> connections) {
        this.connections = connections;
    }

    @Override
    public RecipeEntity create(RecipeEntity entity) {
        var sql = """
                INSERT INTO Recipe(guid, name, description, preparationTime, portions, categoryId)
                VALUES (?, ?, ?, ?, ?, ?)
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, entity.guid());
            statement.setString(2, entity.name());
            statement.setString(3, entity.description());
            statement.setInt(4, entity.preparationTime());
            statement.setInt(5, entity.portions());
            statement.setLong(6, entity.categoryId());
            statement.executeUpdate();

            try (var keyResultSet = statement.getGeneratedKeys()) {
                long recipeId;

                if (keyResultSet.next()) {
                    recipeId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + entity);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + entity);
                }

                return findById(recipeId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + entity, ex);
        }
    }

    @Override
    public Collection<RecipeEntity> findAll() {
        var sql = "SELECT id, guid, name, description, preparationTime, portions, categoryId FROM Recipe";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {

            List<RecipeEntity> recipes = new ArrayList<>();
            try (var resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    var recipe = recipeFromResultSet(resultSet);
                    recipes.add(recipe);
                }
            }

            return recipes;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load all recipes", ex);
        }
    }

    private RecipeEntity recipeFromResultSet(ResultSet resultSet) throws SQLException {
        return new RecipeEntity(
                resultSet.getLong("id"),
                resultSet.getString("guid"),
                resultSet.getString("name"),
                getIngredientsForRecipe(resultSet.getLong("id")),
                resultSet.getString("description"),
                resultSet.getInt("preparationTime"),
                resultSet.getInt("portions"),
                resultSet.getLong("categoryId")
        );
    }

    private List<Long> getIngredientsForRecipe(long recipeId) {
        var sql = "SELECT ingredientId FROM RecipeIngredientWithAmount WHERE recipeId = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, recipeId);
            var resultSet = statement.executeQuery();
            List<Long> ingredientIds = new ArrayList<>();
            while (resultSet.next()) {
                ingredientIds.add(resultSet.getLong("ingredientId"));
            }
            return ingredientIds;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to get ingredients for recipe with id: " + recipeId, ex);
        }
    }

    @Override
    public Optional<RecipeEntity> findById(long id) {
        var sql = "SELECT id, guid, name, description, preparationTime, portions, categoryId FROM Recipe WHERE id = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(recipeFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load recipe by id", ex);
        }
    }

    @Override
    public Optional<RecipeEntity> findByGuid(String guid) {
        var sql = "SELECT id, guid, name, description, preparationTime, portions, categoryId FROM Recipe WHERE guid = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(recipeFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load recipe by guid", ex);
        }
    }

    @Override
    public Optional<RecipeEntity> findByName(String name) {
        var sql = "SELECT id, guid, name, description, preparationTime, portions, categoryId FROM Recipe WHERE name = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, name);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(recipeFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load recipe by guid", ex);
        }
    }

    @Override
    public RecipeEntity update(RecipeEntity entity) {
        var sql = """
                UPDATE Recipe
                SET guid = ?,
                    name = ?,
                    description = ?,
                    preparationTime = ?,
                    portions = ?,
                    categoryId = ?
                WHERE id = ?
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, entity.guid());
            statement.setString(2, entity.name());
            statement.setString(3, entity.description());
            statement.setInt(4, entity.preparationTime());
            statement.setInt(5, entity.portions());
            statement.setLong(6, entity.categoryId());
            statement.setLong(7, entity.id());
            statement.executeUpdate();

            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update recipe: " + entity, ex);
        }
    }

    private void deleteRecipeIngredientWithAmount(Connection connection, long recipeId) throws SQLException {
        var sql = "DELETE FROM RecipeIngredientWithAmount WHERE recipeId = ?";
        try (var statement = connection.prepareStatement(sql)) {
            statement.setLong(1, recipeId);
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        var sql = "DELETE FROM Recipe WHERE guid = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            var recipeId = findIdByGuid(guid).orElseThrow(() -> new DataStorageException("Recipe not found with guid: " + guid));

            deleteRecipeIngredientWithAmount(connection.use(), recipeId);

            statement.setString(1, guid);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete recipe by guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM Recipe";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all recipes", ex);
        }
    }

    @Override
    public boolean existsByGuid(String guid) {
        var sql = "SELECT COUNT(*) AS count FROM Recipe WHERE guid = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count") > 0;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to check if recipe exists by guid: " + guid, ex);
        }
    }

    private Optional<Long> findIdByGuid(String guid) {
        var sql = "SELECT id FROM Recipe WHERE guid = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(resultSet.getLong("id"));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to find recipe ID by guid", ex);
        }
    }
}
