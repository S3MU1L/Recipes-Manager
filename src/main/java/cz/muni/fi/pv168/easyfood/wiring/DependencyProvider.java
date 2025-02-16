package cz.muni.fi.pv168.easyfood.wiring;


import cz.muni.fi.pv168.easyfood.business.model.IngredientWithAmount;
import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.CategoryDao;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.IngredientDao;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.IngredientWithAmountDao;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.RecipeDao;
import cz.muni.fi.pv168.easyfood.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper.IngredientMapper;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper.IngredientWithAmountMapper;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper.RecipeMapper;

/**
 * Interface for instance wiring
 */
public interface DependencyProvider {

    public DatabaseManager getDatabaseManager();

    Repository<Recipe> getRecipeRepository();

    Repository<Ingredient> getIngredientRepository();

    Repository<Category> getCategoryRepository();

    Repository<Unit> getUnitRepository();

    Repository<IngredientWithAmount> getIngredientsWithAmountRepository();

    IngredientWithAmountDao getIngredientWithAmountDao();

    public IngredientWithAmountMapper getIngredientWithAmountMapper();

    public RecipeMapper getRecipeMapper();

    public IngredientMapper getIngredientMapper();

    public RecipeDao getRecipeDao();

    public IngredientDao getIngredientDao();

    public CategoryDao getCategoryDao();

}