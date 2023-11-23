package cz.muni.fi.pv168.easyfood.wiring;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.business.service.crud.CrudService;
import cz.muni.fi.pv168.easyfood.business.service.export.ExportService;
import cz.muni.fi.pv168.easyfood.business.service.export.ImportService;
import cz.muni.fi.pv168.easyfood.business.service.validation.Validator;
import cz.muni.fi.pv168.easyfood.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.easyfood.storage.sql.db.TransactionExecutor;

/**
 * Interface for instance wiring
 */
public interface DependencyProvider {

    DatabaseManager getDatabaseManager();

    Repository<Department> getDepartmentRepository();

    Repository<Employee> getEmployeeRepository();

    TransactionExecutor getTransactionExecutor();

    CrudService<Employee> getEmployeeCrudService();

    CrudService<Department> getDepartmentCrudService();

    ImportService getImportService();

    ExportService getExportService();

    Validator<Employee> getEmployeeValidator();
}

