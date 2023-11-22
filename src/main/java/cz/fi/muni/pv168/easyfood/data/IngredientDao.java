package cz.fi.muni.pv168.easyfood.data;

import cz.fi.muni.pv168.easyfood.model.Ingredient;
import cz.fi.muni.pv168.easyfood.model.Unit;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public final class IngredientDao extends Dao implements DataAccessObject<Ingredient>{

    public IngredientDao(DataSource dataSource, TestDataGenerator testDataGenerator) {
        super(dataSource);
        initTable(testDataGenerator);
    }

    @Override
    public Void create(Ingredient ingredient) {
        if (ingredient.getID() != null) {
            throw new IllegalArgumentException("Create: Ingredient already has ID: " + ingredient);
        }
        try (var connection = getConnection();
             var st = connection.prepareStatement(
                 "INSERT INTO INGREDIENT (NAME, CALORIES, UNIT) VALUES (?, ?, ?)",
                     Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, ingredient.getName());
            st.setDouble(2, ingredient.getCalories());
            st.setString(3, ingredient.getUnit().name());
            st.executeUpdate();
            try (var rs = st.getGeneratedKeys()){
                if (rs.next()) {
                    ingredient.setID(rs.getLong(1));
                } else {
                    throw new DataAccessException("No generated ID returned for: " + ingredient);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to store ingredient " + ingredient, ex);
        }
        return null;
    }

    @Override
    public List<Ingredient> findAll() {
        try (var connection = getConnection();
             var st = connection.prepareStatement(
                     "SELECT ID, NAME, CALORIES, UNIT FROM INGREDIENT")) {
            return find(st);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load all ingredients", ex);
        }
    }

    public Ingredient findById(long ID) {
        try (var connection = getConnection();
             var st = connection.prepareStatement(
                     "SELECT ID, NAME, CALORIES, UNIT FROM INGREDIENT WHERE ID = ?")) {
            st.setLong(1, ID);
            var ingredients = find(st);
            if (ingredients.isEmpty()) {
                return null;
            }
            return ingredients.get(0);
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to load ingredient with ID " + ID, ex);
        }
    }

    private List<Ingredient> find(PreparedStatement st) throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        try (var rs = st.executeQuery()) {
            while (rs.next()) {
                Ingredient ingredient = new Ingredient(
                        rs.getString("NAME"),
                        rs.getDouble("CALORIES"),
                        Unit.valueOf(rs.getString("UNIT")));
                ingredient.setID(rs.getLong("ID"));
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }

    @Override
    public Void update(Ingredient entity) {
        throw new UnsupportedOperationException("Operation is not implemented.");
    }

    @Override
    public Void delete(Ingredient ingredient) {
        if (ingredient.getID() == null) {
            throw new IllegalArgumentException("Delete: ingredient doesn't have ID: " + ingredient);
        }
        try (var connection = getConnection();
             var st = connection.prepareStatement(
                     "DELETE FROM INGREDIENT WHERE ID = ?")) {
            st.setLong(1, ingredient.getID());
            if (st.executeUpdate() == 0) {
                throw new DataAccessException("No ingredient with this ID: " + ingredient);
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to delete ingredient " + ingredient, ex);
        }
        return null;
    }

    private void initTable(TestDataGenerator testDataGenerator) {
        if (!tableExists("APP", "INGREDIENT")) {
            createTable();
            if (testDataGenerator != null) {
                testDataGenerator.createTestIngredients(10).forEach(this::create);
            }
        }
    }

    private void createTable() {
        try (var connection = getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("CREATE TABLE APP.INGREDIENT (" +
                    "ID BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY," +
                    "NAME VARCHAR(20) NOT NULL," +
                    "CALORIES DECIMAL," +
                    "UNIT VARCHAR(10)" +
                    ")");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to create INGREDIENT table", ex);
        }
    }

    public void dropTable() {
        try (var connection = getConnection();
             var st = connection.createStatement()) {

            st.executeUpdate("DROP TABLE APP.INGREDIENT");
        } catch (SQLException ex) {
            throw new DataAccessException("Failed to drop INGREDIENT table", ex);
        }
    }
}
