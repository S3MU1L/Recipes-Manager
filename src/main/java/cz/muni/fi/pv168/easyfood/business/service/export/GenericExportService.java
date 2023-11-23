package cz.muni.fi.pv168.easyfood.business.service.export;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.service.crud.CrudService;
import cz.muni.fi.pv168.easyfood.business.service.export.batch.Batch;
import cz.muni.fi.pv168.easyfood.business.service.export.batch.BatchExporter;
import cz.muni.fi.pv168.easyfood.business.service.export.batch.BatchOperationException;
import cz.muni.fi.pv168.easyfood.business.service.export.format.Format;
import cz.muni.fi.pv168.easyfood.business.service.export.format.FormatMapping;

import java.util.Collection;

/**
 * Generic synchronous implementation of the {@link ExportService}
 */
public class GenericExportService implements ExportService {

    private final CrudService<Employee> employeeCrudService;
    private final CrudService<Department> departmentCrudService;
    private final FormatMapping<BatchExporter> exporters;

    public GenericExportService(
            CrudService<Employee> employeeCrudService,
            CrudService<Department> departmentCrudService,
            Collection<BatchExporter> exporters
    ) {
        this.employeeCrudService = employeeCrudService;
        this.departmentCrudService = departmentCrudService;
        this.exporters = new FormatMapping<>(exporters);
    }

    @Override
    public Collection<Format> getFormats() {
        return exporters.getFormats();
    }

    @Override
    public void exportData(String filePath) {
        var exporter = getExporter(filePath);

        var batch = new Batch(departmentCrudService.findAll(), employeeCrudService.findAll());
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
