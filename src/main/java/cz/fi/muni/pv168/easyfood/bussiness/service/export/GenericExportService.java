package cz.fi.muni.pv168.easyfood.bussiness.service.export;


import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.Recipe;
import cz.fi.muni.pv168.easyfood.bussiness.service.crud.CrudService;
import cz.fi.muni.pv168.easyfood.bussiness.service.export.batch.Batch;
import cz.fi.muni.pv168.easyfood.bussiness.service.export.batch.BatchExporter;
import cz.fi.muni.pv168.easyfood.bussiness.service.export.batch.BatchOperationException;
import cz.fi.muni.pv168.easyfood.bussiness.service.export.format.Format;
import cz.fi.muni.pv168.easyfood.bussiness.service.export.format.FormatMapping;

import java.util.Collection;

/**
 * Generic synchronous implementation of the {@link ExportService}
 */
public class GenericExportService implements ExportService {

    private final CrudService<Recipe> recipeCrudService;
    private final CrudService<Ingredient> ingredientCrudService;
    private final FormatMapping<BatchExporter> exporters;

    public GenericExportService(
            CrudService<Recipe> recipeCrudService,
            CrudService<Ingredient> ingredientCrudService,
            Collection<BatchExporter> exporters
    ) {
        this.recipeCrudService = recipeCrudService;
        this.ingredientCrudService = ingredientCrudService;
        this.exporters = new FormatMapping<>(exporters);
    }

    @Override
    public Collection<Format> getFormats() {
        return exporters.getFormats();
    }

    @Override
    public void exportData(String filePath) {
        var exporter = getExporter(filePath);

        var batch = new Batch(recipeCrudService.findAll(), ingredientCrudService.findAll());
        exporter.exportBatch(batch, filePath);
    }

    private BatchExporter getExporter(String filePath) {
        var extension = filePath.substring(filePath.lastIndexOf('.') + 1);
        var importer = exporters.findByExtension(extension);
        if (importer == null)
            throw new BatchOperationException("Extension %s has no registered formatter".formatted(extension));
        return importer;
    }
}
