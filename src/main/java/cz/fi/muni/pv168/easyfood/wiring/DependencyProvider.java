package cz.fi.muni.pv168.easyfood.wiring;


import cz.fi.muni.pv168.easyfood.bussiness.repository.Repository;
import cz.fi.muni.pv168.easyfood.bussiness.service.crud.CrudService;
import cz.fi.muni.pv168.easyfood.bussiness.service.validation.Validator;
import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.IngredientWithAmount;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.storage.sql.db.TransactionExecutor;

/**
 * Interface for instance wiring
 */
public interface DependencyProvider {

    Repository<Recipe> getRecipeRepository();

    Repository<Ingredient> getIngredientRepository();

    Repository<Category> getCategoryRepository();

    Repository<IngredientWithAmount> getIngredientWithAmountRepository();

    TransactionExecutor getTransactionExecutor();

    CrudService<Recipe> getRecipeCrudService();

    CrudService<Ingredient> getIngredientCrudService();

    CrudService<Category> getCategoryCrudService();

    CrudService<IngredientWithAmount> getIngredientWithAmountCrudService();

    Validator<Recipe> getRecipeValidator();
}

