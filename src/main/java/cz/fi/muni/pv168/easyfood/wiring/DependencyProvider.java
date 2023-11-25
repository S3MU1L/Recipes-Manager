package cz.fi.muni.pv168.easyfood.wiring;


import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;
import cz.fi.muni.pv168.easyfood.bussiness.repository.Repository;
import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.storage.sql.db.DatabaseManager;

/**
 * Interface for instance wiring
 */
public interface DependencyProvider {

    public DatabaseManager getDatabaseManager();

    Repository<Recipe> getRecipeRepository();

    Repository<Ingredient> getIngredientRepository();

    Repository<Category> getCategoryRepository();

    Repository<Unit> getUnitRepository();

}

