package cz.fi.muni.pv168.easyfood.wiring;


import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;
import cz.fi.muni.pv168.easyfood.bussiness.repository.Repository;
import cz.fi.muni.pv168.easyfood.storage.sql.CategorySqlRepository;
import cz.fi.muni.pv168.easyfood.storage.sql.IngredientSqlRepository;
import cz.fi.muni.pv168.easyfood.storage.sql.RecipeSqlRepository;
import cz.fi.muni.pv168.easyfood.storage.sql.UnitSqlRepository;
import cz.fi.muni.pv168.easyfood.storage.sql.dao.CategoryDao;
import cz.fi.muni.pv168.easyfood.storage.sql.dao.IngredientDao;
import cz.fi.muni.pv168.easyfood.storage.sql.dao.IngredientWithAmountDao;
import cz.fi.muni.pv168.easyfood.storage.sql.dao.RecipeDao;
import cz.fi.muni.pv168.easyfood.storage.sql.dao.UnitDao;
import cz.fi.muni.pv168.easyfood.storage.sql.db.DatabaseManager;
import cz.fi.muni.pv168.easyfood.storage.sql.entity.mapper.CategoryMapper;
import cz.fi.muni.pv168.easyfood.storage.sql.entity.mapper.IngredientMapper;
import cz.fi.muni.pv168.easyfood.storage.sql.entity.mapper.IngredientWitAmountMapper;
import cz.fi.muni.pv168.easyfood.storage.sql.entity.mapper.RecipeMapper;
import cz.fi.muni.pv168.easyfood.storage.sql.entity.mapper.UnitMapper;

/**
 * Common dependency provider for both production and test environment.
 */
public class CommonDependencyProvider implements DependencyProvider {

    private final Repository<Recipe> recipes;
    private final Repository<Ingredient> ingredients;
    private final Repository<Category> categories;
    private final Repository<Unit> units;
    private final DatabaseManager databaseManager;

    public CommonDependencyProvider(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;

        var categoryMapper = new CategoryMapper();
        var categoryDao = new CategoryDao(databaseManager::getConnectionHandler);

        var unitMapper = new UnitMapper();
        var unitDao = new UnitDao(databaseManager::getConnectionHandler);

        var ingredientMapper = new IngredientMapper(unitDao, unitMapper);
        var ingredientDao = new IngredientDao(databaseManager::getConnectionHandler);

        var ingredientWithAmountMapper = new IngredientWitAmountMapper(ingredientDao, ingredientMapper);
        var ingredientWithAmountDao = new IngredientWithAmountDao(databaseManager::getConnectionHandler);

        var recipeMapper = new RecipeMapper(categoryDao, categoryMapper, ingredientWithAmountDao, ingredientWithAmountMapper);
        var recipeDao = new RecipeDao(databaseManager::getConnectionHandler);

        this.categories = new CategorySqlRepository(
                categoryDao,
                categoryMapper
        );

        this.units = new UnitSqlRepository(
                unitDao,
                unitMapper
        );

        this.ingredients = new IngredientSqlRepository(
                ingredientDao,
                ingredientMapper
        );

        this.recipes = new RecipeSqlRepository(
                recipeDao,
                recipeMapper
        );

    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public Repository<Recipe> getRecipeRepository() {
        return recipes;
    }

    @Override
    public Repository<Ingredient> getIngredientRepository() {
        return ingredients;
    }

    @Override
    public Repository<Category> getCategoryRepository() {
        return categories;
    }

    @Override
    public Repository<Unit> getUnitRepository() {
        return units;
    }
}
