package cz.fi.muni.pv168.easyfood.bussiness.service.export;


import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.bussiness.service.crud.CrudService;
import cz.fi.muni.pv168.easyfood.bussiness.service.export.batch.BatchImporter;
import cz.fi.muni.pv168.easyfood.bussiness.service.export.batch.BatchOperationException;
import cz.fi.muni.pv168.easyfood.bussiness.service.export.format.Format;
import cz.fi.muni.pv168.easyfood.bussiness.service.export.format.FormatMapping;

import java.util.Collection;

/**
 * Generic synchronous implementation of the {@link ImportService}.
 */
public class GenericImportService implements ImportService {

    private final CrudService<Recipe> recipeCrudService;
    private final CrudService<Ingredient> ingredientCrudService;
    private final FormatMapping<BatchImporter> importers;

    public GenericImportService(
            CrudService<Recipe> recipeCrudService,
            CrudService<Ingredient> ingredientCrudService,
            Collection<BatchImporter> importers
    ) {
        this.recipeCrudService = recipeCrudService;
        this.ingredientCrudService = ingredientCrudService;
        this.importers = new FormatMapping<>(importers);
    }

    @Override
    public void importData(String filePath) {
        recipeCrudService.deleteAll();
        ingredientCrudService.deleteAll();

        var batch = getImporter(filePath).importBatch(filePath);

        batch.recipes().forEach(this::createRecipe);
        batch.ingredients().forEach(this::createIngredient);
    }

    private void createRecipe(Recipe recipe) {
        recipeCrudService.create(recipe)
                .intoException();
    }

    private void createIngredient(Ingredient ingredient) {
        ingredientCrudService.create(ingredient)
                .intoException();
    }

    @Override
    public Collection<Format> getFormats() {
        return importers.getFormats();
    }

    private BatchImporter getImporter(String filePath) {
        var extension = filePath.substring(filePath.lastIndexOf('.') + 1);
        var importer = importers.findByExtension(extension);
        if (importer == null) {
            throw new BatchOperationException("Extension %s has no registered formatter".formatted(extension));
        }

        return importer;
    }
}
