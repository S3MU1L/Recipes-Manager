package cz.fi.muni.pv168.easyfood.data;

import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.IngredientAndAmount;
import cz.fi.muni.pv168.easyfood.model.Recipe;
import cz.fi.muni.pv168.easyfood.model.Unit;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RecipeDao extends Dao implements DataAccessObject<Recipe> {
    private final IngredientDao ingredientDao;

    public RecipeDao(DataSource dataSource, IngredientDao ingredientDao, TestDataGenerator testDataGenerator) {
        super(dataSource);
        this.ingredientDao = ingredientDao;
        initTable(testDataGenerator);
    }

    @Override
    public Void create(Recipe recipe) {
        if (recipe.getID() != null) {
            throw new IllegalArgumentException("Create: Recipe already has ID: " + recipe);
        }
        if (recipe.getIngredients().stream().anyMatch(ingredient -> ingredient.getID() == null)) {
            throw new IllegalArgumentException("Create: Recipe contains not yet created ingredients " + recipe);
        }
        if (recipe.getIngredients().stream().mapToLong(IngredientAndAmount::getID).distinct().count()
                != recipe.getIngredients().size()) {
            throw new IllegalArgumentException("Create: Recipe contains duplicate ingredients " + recipe);
        }
        try ( var connection = getConnection();
             var st = connection.prepareStatement(
                     "INSERT INTO RECIPE (NAME, DIRECTIONS, PREPTIME, PORTIONS) VALUES (?, ?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, recipe.getName());
            st.setString(2, recipe.getDirections());
            st.setInt(3, recipe.getPrepTime());
            st.setInt(4, recipe.getPortions());
            st.executeUpdate();
            try (var rs = st.getGeneratedKeys()){
                if (rs.next()) {
                    recipe.setID(rs.getLong(1));
                } else {
                    throw new DataAccessException("No generated ID returned for: " + recipe);
                }
            }
            createRecipeIngredients(recipe, connection);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store recipe " + recipe, ex);
        }
        return null;
    }

    private void createRecipeIngredients(Recipe recipe, Connection connection) throws SQLException {
        try (var st = connection.prepareStatement(
                     "INSERT INTO RECIPE_INGREDIENT (RECIPE_ID, INGREDIENT_ID, AMOUNT) VALUES (?, ?, ?)") ) {
            st.setLong(1, recipe.getID());
            var ingredients = recipe.getIngredients();
            for (int i = 0; i < ingredients.size(); i++) {
                st.setLong(2, ingredients.get(i).getID());
                st.setDouble(3, ingredients.get(i).getAmount());
                if (st.executeUpdate() == 0) {
                    throw new DataAccessException("Cannot insert ingredient (" + ingredients.get(i) +
                            ") to recipe (" + recipe + ")");
                }
            }
        }
    }

    @Override
    public Void update(Recipe recipe) {
        if (recipe.getID() == null) {
            throw new IllegalArgumentException("Cannot update recipe without ID: " + recipe);
        }
        try (var connection = getConnection()) {
            deleteRecipeIngredients(recipe, connection);
            createRecipeIngredients(recipe, connection);
            try (var st = connection.prepareStatement(
                    "UPDATE RECIPE SET NAME = ?, DIRECTIONS = ?, PREPTIME = ?, PORTIONS = ? WHERE ID = ?")) {
                st.setString(1, recipe.getName());
                st.setString(2, recipe.getDirections());
                st.setInt(3, recipe.getPrepTime());
                st.setInt(4, recipe.getPortions());
                st.setLong(5, recipe.getID());
                if (st.executeUpdate() == 0) {
                    throw new DataAccessException("No recipe to update: " + recipe);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to update recipe " + recipe.toString(), ex);
        }
        return null;
    }

    @Override
    public Void delete(Recipe recipe) {
        if (recipe.getID() == null) {
            throw new IllegalArgumentException("Cannot delete recipe without ID: " + recipe);
        }
        try (var connection = getConnection()) {
            deleteRecipeIngredients(recipe, connection);
            try (var st = connection.prepareStatement(
                     "DELETE FROM RECIPE WHERE ID = ?")) {
                st.setLong(1, recipe.getID());
                if (st.executeUpdate() == 0) {
                    throw new DataAccessException("Recipe wan not deleted: " + recipe);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete recipe " + recipe.toString(), ex);
        }
        return null;
    }

    private void deleteRecipeIngredients(Recipe recipe, Connection connection) throws SQLException {
        try (var st = connection.prepareStatement(
                "DELETE FROM RECIPE_INGREDIENT WHERE RECIPE_ID = ?")) {
            st.setLong(1, recipe.getID());
            st.executeUpdate();
        }
    }

    private List<Recipe> find(PreparedStatement st, Connection connection) throws SQLException {
        List<Recipe> recipes = new ArrayList<>();
        try (var rs = st.executeQuery()) {
            while (rs.next()) {
                Recipe recipe = new Recipe(
                        rs.getString("NAME"),
                        new ArrayList<>(),
                        rs.getString("DIRECTIONS"),
                        rs.getInt("PREPTIME"),
                        rs.getInt("PORTIONS"));
                recipe.setID(rs.getLong("ID"));
                fillIngredientsWithAmounts(recipe, connection);
                recipes.add(recipe);
            }
        }
        return recipes;
    }

    @Override
    public List<Recipe> findAll() {
        try (var connection = getConnection();
             var st = connection.prepareStatement(
                     "SELECT ID, NAME, DIRECTIONS, PREPTIME, PORTIONS FROM RECIPE")) {
            return find(st, connection);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all recipes", ex);
        }
    }

    public Recipe findById(long ID) {
        try (var connection = getConnection();
             var st = connection.prepareStatement(
                     "SELECT ID, NAME, DIRECTIONS, PREPTIME FROM RECIPE WHERE ID = ?")) {
            st.setLong(1, ID);
            var recipes = find(st, connection);
            if (recipes.isEmpty()) {
                return null;
            }
            return recipes.get(0);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load recipe with ID " + ID, ex);
        }
    }

    public List<Long> getContainingIDs(Ingredient ingredient) {
        if (ingredient.getID() == null) {
            throw new IllegalArgumentException("Ingredient without ID: " + ingredient);
        }
        try (var connection = getConnection();
             var st = connection.prepareStatement(
                     "SELECT DISTINCT RECIPE_ID FROM RECIPE_INGREDIENT WHERE INGREDIENT_ID = ?")) {
            st.setLong(1, ingredient.getID());
            List<Long> IDs = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    IDs.add(rs.getLong("RECIPE_ID"));
                }
            }
            return IDs;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to find recipes containing " + ingredient, ex);
        }
    }

    public List<Long> getUsedIngredientsIDs() {
        try (var connection = getConnection();
             var st = connection.prepareStatement(
                     "SELECT INGREDIENT_ID FROM RECIPE_INGREDIENT")) {
            List<Long> ingredientIDs = new ArrayList<>();
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    ingredientIDs.add(rs.getLong("INGREDIENT_ID"));
                }
            }
            return ingredientIDs;
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all recipes", ex);
        }
    }

    private void fillIngredientsWithAmounts(Recipe recipe, Connection connection) {
        try (var st = connection.prepareStatement(
                     "SELECT INGREDIENT_ID, NAME, CALORIES, UNIT, AMOUNT " +
                             "FROM RECIPE_INGREDIENT RIGHT OUTER JOIN INGREDIENT ON " +
                             "RECIPE_INGREDIENT.INGREDIENT_ID = INGREDIENT.ID WHERE RECIPE_ID = ?")) {
            st.setLong(1, recipe.getID());
            try (var rs = st.executeQuery()) {
                while (rs.next()) {
                    Ingredient ingredient = new Ingredient(
                            rs.getString("NAME"),
                            rs.getDouble("CALORIES"),
                            Unit.valueOf(rs.getString("UNIT"))
                    );
                    ingredient.setID(rs.getLong("INGREDIENT_ID"));
                    recipe.addIngredient(ingredient, rs.getDouble("AMOUNT"));
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load ingredients for " + recipe, ex);
        }
    }

    private void initTable(TestDataGenerator testDataGenerator) {
        // they are created together, so check for only one of them is enough
        if (!tableExists("APP", "RECIPE")) {
            createTables();
            if (testDataGenerator != null) {
                testDataGenerator.createTestRecipes(5).forEach(recipe -> {
                            recipe.getIngredients().stream().map(IngredientAndAmount::getIngredient).forEach(ingredientDao::create);
                            create(recipe);
                        }
                );
            }
        }
    }

    private void createTables() {
        try (var connection = getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("CREATE TABLE APP.RECIPE (" +
                    "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "NAME VARCHAR(100) NOT NULL," +
                    "DIRECTIONS VARCHAR(1000)," +
                    "PREPTIME INT," +
                    "PORTIONS INT" +
                    ")");
            st.executeUpdate("CREATE TABLE APP.RECIPE_INGREDIENT (" +
                    "RECIPE_ID BIGINT NOT NULL," +
                    "INGREDIENT_ID BIGINT NOT NULL," +
                    "AMOUNT DECIMAL," +
                    "DIRECTIONS VARCHAR(1000)," +
                    "PRIMARY KEY (RECIPE_ID, INGREDIENT_ID)," +
                    "FOREIGN KEY (RECIPE_ID) REFERENCES RECIPE (ID)," +
                    "FOREIGN KEY (INGREDIENT_ID) REFERENCES INGREDIENT (ID)" +
                    ")");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create RECIPE table", ex);
        }
    }

    public void dropTables() {
        try (var connection = getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("DROP TABLE APP.RECIPE_INGREDIENT");
            st.executeUpdate("DROP TABLE APP.RECIPE");

        } catch (SQLException ex) {
            throw new DataAccessException("Failed to drop RECIPE and RECIPE_INGREDIENT table", ex);
        }
    }
}
