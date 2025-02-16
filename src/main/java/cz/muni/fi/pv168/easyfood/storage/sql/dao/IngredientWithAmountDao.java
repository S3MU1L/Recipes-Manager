package cz.muni.fi.pv168.easyfood.storage.sql.dao;

import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.storage.DataStorageException;
import cz.muni.fi.pv168.easyfood.storage.sql.db.ConnectionHandler;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.IngredientWithAmountEntity;

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

    public IngredientWithAmountDao(
            Supplier<ConnectionHandler> connections
    ) {
        this.connections = connections;
    }

    @Override
    public IngredientWithAmountEntity create(IngredientWithAmountEntity entity) {
        // NOT IMPLEMENTED IN THIS DAO
        return null;
    }

    @Override
    public Collection<IngredientWithAmountEntity> findAll() {
        var sql = "SELECT id, guid, recipeId, ingredientId, amount FROM RecipeIngredientWithAmount";
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
            throw new DataStorageException("Failed to load all recipe ingredients with amount", ex);
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

    public Optional<IngredientWithAmountEntity> findByRecipeAndIngredientId(long recipeId, long ingredientId) {
        var sql = "SELECT id, guid, recipeId, ingredientId, amount FROM RecipeIngredientWithAmount WHERE recipeId = ? AND ingredientId = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, recipeId);
            statement.setLong(2, ingredientId);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(ingredientWithAmountFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load recipe ingredient with amount by id", ex);
        }
    }


    @Override
    public Optional<IngredientWithAmountEntity> findById(long id) {
        var sql = "SELECT id, guid, recipeId, ingredientId, amount FROM RecipeIngredientWithAmount WHERE id = ?";
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
            throw new DataStorageException("Failed to load recipe ingredient with amount by id", ex);
        }
    }

    @Override
    public Optional<IngredientWithAmountEntity> findByGuid(String guid) {
        var sql = "SELECT id, guid, recipeId, ingredientId, amount FROM RecipeIngredientWithAmount WHERE guid = ?";
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
            throw new DataStorageException("Failed to load recipe ingredient with amount by guid", ex);
        }
    }

    @Override
    public Optional<IngredientWithAmountEntity> findByName(String name) {
        var sql = "SELECT id, guid, recipeId, ingredientId, amount FROM RecipeIngredientWithAmount WHERE name = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, name);
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(ingredientWithAmountFromResultSet(resultSet));
            } else {
                return Optional.empty();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load recipe ingredient with amount by guid", ex);
        }
    }

    public List<IngredientWithAmountEntity> findIngredientsOfRecipe(Long id) {
        var sql = "SELECT id, guid, recipeId, ingredientId, amount FROM RecipeIngredientWithAmount WHERE recipeId = ?";
        List<IngredientWithAmountEntity> ingredientsList = new ArrayList<>();

        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, id);
            var resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ingredientsList.add(ingredientWithAmountFromResultSet(resultSet));
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to load recipe ingredients with amount by guid", ex);
        }

        return ingredientsList;
    }


    @Override
    public IngredientWithAmountEntity update(IngredientWithAmountEntity entity) {
        var sql = "UPDATE RecipeIngredientWithAmount SET recipeId = ?, ingredientId = ?, amount = ? WHERE id = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setLong(1, entity.ingredientId());
            statement.setLong(2, entity.ingredientId());
            statement.setDouble(3, entity.amount());
            statement.setLong(4, entity.id());
            statement.executeUpdate();
            return entity;
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to update recipe ingredient with amount: " + entity, ex);
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        var sql = "DELETE FROM RecipeIngredientWithAmount WHERE guid = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, guid);
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete recipe ingredient with amount by guid: " + guid, ex);
        }
    }

    @Override
    public void deleteAll() {
        var sql = "DELETE FROM RecipeIngredientWithAmount";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to delete all recipe ingredients with amount", ex);
        }
    }

    @Override
    public boolean existsByGuid(String guid) {
        var sql = "SELECT id FROM RecipeIngredientWithAmount WHERE guid = ?";
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql)
        ) {
            statement.setString(1, guid);
            var resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to check if recipe ingredient with amount exists: " + guid, ex);
        }
    }

    public IngredientWithAmountEntity addRecipeIngredient(IngredientWithAmount ingredient, Long recipeId, Long ingredientId) {
        System.out.println("adding recipe-ingredient" + recipeId + " " + ingredientId + " " + ingredient.getAmount());
        var sql = """
                INSERT INTO RecipeIngredientWithAmount(guid, recipeId, ingredientId, amount)
                VALUES (?, ?, ?, ?)
                """;
        try (
                var connection = connections.get();
                var statement = connection.use().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, ingredient.getGuid());
            statement.setLong(2, recipeId);
            statement.setLong(3, ingredientId);
            statement.setDouble(4, ingredient.getAmount());
            statement.executeUpdate();

            try (var keyResultSet = statement.getGeneratedKeys()) {
                long recipeIngredientWithAmountId;

                if (keyResultSet.next()) {
                    recipeIngredientWithAmountId = keyResultSet.getLong(1);
                } else {
                    throw new DataStorageException("Failed to fetch generated key for: " + ingredient);
                }
                if (keyResultSet.next()) {
                    throw new DataStorageException("Multiple keys returned for: " + ingredient);
                }

                System.out.println("executed insert");
                return findById(recipeIngredientWithAmountId).orElseThrow();
            }
        } catch (SQLException ex) {
            throw new DataStorageException("Failed to store: " + ingredient, ex);
        }

    }
}
