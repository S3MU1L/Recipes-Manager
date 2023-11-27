package cz.muni.fi.pv168.easyfood.wiring;


import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.storage.sql.CategorySqlRepository;
import cz.muni.fi.pv168.easyfood.storage.sql.IngredientSqlRepository;
import cz.muni.fi.pv168.easyfood.storage.sql.IngredientWithAmountSqlRepository;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.IngredientWithAmountDao;
import cz.muni.fi.pv168.easyfood.storage.sql.RecipeSqlRepository;
import cz.muni.fi.pv168.easyfood.storage.sql.UnitSqlRepository;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.CategoryDao;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.IngredientDao;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.RecipeDao;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.UnitDao;
import cz.muni.fi.pv168.easyfood.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper.CategoryMapper;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper.IngredientMapper;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper.IngredientWithAmountMapper;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper.RecipeMapper;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper.UnitMapper;

/**
 * Common dependency provider for both production and test environment.
 */
public class CommonDependencyProvider implements DependencyProvider {

    private final Repository<Recipe> recipes;
    private final Repository<Ingredient> ingredients;
    private final Repository<Category> categories;
    private final Repository<Unit> units;
    private final Repository<IngredientWithAmount> ingredientsWithAmount;
    private final DatabaseManager databaseManager;
    private final IngredientWithAmountDao ingredientWithAmountDao;
    private final RecipeDao recipeDao;
    private final IngredientDao ingredientDao;
    private final CategoryDao categoryDao;
    private final IngredientWithAmountMapper ingredientWithAmountMapper;
    private final RecipeMapper recipeMapper;


    private final IngredientMapper ingredientMapper;

    public CommonDependencyProvider(DatabaseManager databaseManager) {
        this.databaseManager = databaseManager;

        var categoryMapper = new CategoryMapper();
        categoryDao = new CategoryDao(databaseManager::getConnectionHandler);

        var unitMapper = new UnitMapper();
        var unitDao = new UnitDao(databaseManager::getConnectionHandler);

        ingredientMapper = new IngredientMapper(unitDao, unitMapper);
        ingredientDao = new IngredientDao(databaseManager::getConnectionHandler);

        ingredientWithAmountMapper = new IngredientWithAmountMapper(ingredientDao, ingredientMapper);
        ingredientWithAmountDao = new IngredientWithAmountDao(databaseManager::getConnectionHandler);

        recipeMapper = new RecipeMapper(categoryDao, categoryMapper, ingredientWithAmountDao, ingredientWithAmountMapper);
        recipeDao = new RecipeDao(databaseManager::getConnectionHandler);

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

        this.ingredientsWithAmount = new IngredientWithAmountSqlRepository(
                ingredientWithAmountDao,
                ingredientWithAmountMapper
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
    public Repository<IngredientWithAmount> getIngredientsWithAmountRepository() {
        return ingredientsWithAmount;
    }

    @Override
    public Repository<Category> getCategoryRepository() {
        return categories;
    }

    @Override
    public Repository<Unit> getUnitRepository() {
        return units;
    }

    @Override
    public IngredientWithAmountDao getIngredientWithAmountDao() {
        return ingredientWithAmountDao;
    }

    public IngredientWithAmountMapper getIngredientWithAmountMapper() {
        return ingredientWithAmountMapper;
    }

    @Override
    public RecipeMapper getRecipeMapper() {
        return recipeMapper;
    }

    @Override
    public IngredientMapper getIngredientMapper() {
        return ingredientMapper;
    }

    @Override
    public RecipeDao getRecipeDao() {
        return recipeDao;
    }

    @Override
    public IngredientDao getIngredientDao() {
        return ingredientDao;
    }

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }
}
