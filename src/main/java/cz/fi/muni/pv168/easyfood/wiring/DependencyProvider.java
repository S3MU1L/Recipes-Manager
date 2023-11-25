package cz.fi.muni.pv168.easyfood.wiring;


import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;
import cz.fi.muni.pv168.easyfood.bussiness.repository.Repository;
import cz.fi.muni.pv168.easyfood.bussiness.service.crud.CrudService;
import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.IngredientWithAmount;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.storage.sql.db.DatabaseManager;
import cz.fi.muni.pv168.easyfood.bussiness.service.export.ExportService;
import cz.fi.muni.pv168.easyfood.bussiness.service.export.ImportService;

/**
 * Interface for instance wiring
 */
public interface DependencyProvider {

    public DatabaseManager getDatabaseManager();

    CrudService<Recipe> getRecipeCrudService();

    CrudService<Ingredient> getIngredientCrudService();

    CrudService<Category> getCategoryCrudService();

    CrudService<IngredientWithAmount> getIngredientWithAmountCrudService();

    CrudService<Unit> getUnitCrudService();

    ImportService getImportService();

    ExportService getExportService();

    Repository<Recipe> getRecipeRepository();

    Repository<Ingredient> getIngredientRepository();

    Repository<Category> getCategoryRepository();

    Repository<Unit> getUnitRepository();

}

