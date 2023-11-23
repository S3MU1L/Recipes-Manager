package cz.muni.fi.pv168.easyfood.business.service.export;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.service.crud.CrudService;
import cz.muni.fi.pv168.easyfood.business.service.export.batch.BatchImporter;
import cz.muni.fi.pv168.easyfood.business.service.export.batch.BatchOperationException;
import cz.muni.fi.pv168.easyfood.business.service.export.format.Format;
import cz.muni.fi.pv168.easyfood.business.service.export.format.FormatMapping;

import java.util.Collection;

/**
 * Generic synchronous implementation of the {@link ImportService}.
 */
public class GenericImportService implements ImportService {

    private final CrudService<Employee> employeeCrudService;
    private final CrudService<Department> departmentCrudService;
    private final FormatMapping<BatchImporter> importers;

    public GenericImportService(
            CrudService<Employee> employeeCrudService,
            CrudService<Department> departmentCrudService,
            Collection<BatchImporter> importers
    ) {
        this.employeeCrudService = employeeCrudService;
        this.departmentCrudService = departmentCrudService;
        this.importers = new FormatMapping<>(importers);
    }

    @Override
    public void importData(String filePath) {
        employeeCrudService.deleteAll();
        departmentCrudService.deleteAll();

        var batch = getImporter(filePath).importBatch(filePath);

        batch.departments().forEach(this::createDepartment);
        batch.employees().forEach(this::createEmployee);
    }

    private void createDepartment(Department department) {
        departmentCrudService.create(department)
                .intoException();
    }

    private void createEmployee(Employee employee) {
        employeeCrudService.create(employee)
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
