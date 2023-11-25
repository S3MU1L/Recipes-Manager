package cz.muni.fi.pv168.easyfood.wiring;


import cz.muni.fi.pv168.easyfood.business.model.Unit;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.business.model.Category;
import cz.muni.fi.pv168.easyfood.business.model.Ingredient;
import cz.muni.fi.pv168.easyfood.business.model.Recipe;
import cz.muni.fi.pv168.easyfood.storage.sql.db.DatabaseManager;

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

