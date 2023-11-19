package cz.fi.muni.pv168.easyfood.wiring;


import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.IngredientWithAmount;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.bussiness.repository.Repository;
import cz.fi.muni.pv168.easyfood.bussiness.service.crud.CrudService;
import cz.fi.muni.pv168.easyfood.bussiness.service.crud.RecipeCrudService;
import cz.fi.muni.pv168.easyfood.bussiness.service.export.ExportService;
import cz.fi.muni.pv168.easyfood.bussiness.service.export.ImportService;
import cz.fi.muni.pv168.easyfood.bussiness.service.validation.IngredientValidator;
import cz.fi.muni.pv168.easyfood.bussiness.service.validation.RecipeValidator;
import cz.fi.muni.pv168.easyfood.storage.sql.RecipeSqlRepository;
import cz.fi.muni.pv168.easyfood.storage.sql.dao.RecipeDao;
import cz.fi.muni.pv168.easyfood.storage.sql.db.DatabaseManager;
import cz.fi.muni.pv168.easyfood.storage.sql.db.TransactionExecutor;
import cz.fi.muni.pv168.easyfood.bussiness.model.UuidGuidProvider;
import cz.fi.muni.pv168.easyfood.storage.sql.db.TransactionManagerImpl;
import cz.fi.muni.pv168.easyfood.storage.sql.db.TransactionExecutorImpl;
import cz.fi.muni.pv168.easyfood.storage.sql.db.TransactionConnectionSupplier;
import cz.fi.muni.pv168.easyfood.storage.sql.entity.mapper.RecipeMapper;

/**
 * Common dependency provider for both production and test environment.
 */
public class CommonDependencyProvider implements DependencyProvider {
    private final Repository<Recipe> recipes;
    private final DatabaseManager databaseManager;
    private final TransactionExecutor transactionExecutor;
    private final CrudService<Recipe> recipeCrudService;
    private final ImportService importService;
    private final ExportService exportService;
    private final RecipeValidator recipeValidator;


    public CommonDependencyProvider(DatabaseManager databaseManager) {
        recipeValidator = new RecipeValidator();
        var ingredientValidator = new IngredientValidator();
        var guidProvider = new UuidGuidProvider();

        this.databaseManager = databaseManager;
        var transactionManager = new TransactionManagerImpl(databaseManager);
        this.transactionExecutor = new TransactionExecutorImpl(transactionManager::beginTransaction);
        var transactionConnectionSupplier = new TransactionConnectionSupplier(transactionManager, databaseManager);

        var recipeMapper = new RecipeMapper();
        var departmentDao = new RecipeDao(transactionConnectionSupplier);

        this.recipes = new RecipeSqlRepository(
                new RecipeDao(transactionConnectionSupplier),
                recipeMapper
        );

        recipeCrudService = new RecipeCrudService(recipes, recipeValidator, guidProvider);
        importService = null;
        exportService = null;
//        exportService = new GenericExportService(recipeCrudService, ingredientCrudService,
//                List.of(new BatchCsvExporter()));
//        importService = new GenericImportService(recipeCrudService, ingredientCrudService,
//                List.of(new BatchCsvImporter()));
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
        return null;
    }

    @Override
    public Repository<Category> getCategoryRepository() {
        return null;
    }

    @Override
    public Repository<IngredientWithAmount> getIngredientWithAmountRepository() {
        return null;
    }

    @Override
    public TransactionExecutor getTransactionExecutor() {
        return transactionExecutor;
    }

    @Override
    public CrudService<Recipe> getRecipeCrudService() {
        return recipeCrudService;
    }

    @Override
    public CrudService<Ingredient> getIngredientCrudService() {
        return null;
    }

    @Override
    public CrudService<Category> getCategoryCrudService() {
        return null;
    }

    @Override
    public CrudService<IngredientWithAmount> getIngredientWithAmountCrudService() {
        return null;
    }

    @Override
    public ImportService getImportService() {
        return null;
    }

    @Override
    public ExportService getExportService() {
        return null;
    }

    @Override
    public RecipeValidator getRecipeValidator() {
        return recipeValidator;
    }
}
