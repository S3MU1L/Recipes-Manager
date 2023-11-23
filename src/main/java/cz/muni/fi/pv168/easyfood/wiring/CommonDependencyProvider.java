package cz.muni.fi.pv168.easyfood.wiring;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.model.UuidGuidProvider;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.business.service.crud.CrudService;
import cz.muni.fi.pv168.easyfood.business.service.crud.DepartmentCrudService;
import cz.muni.fi.pv168.easyfood.business.service.crud.EmployeeCrudService;
import cz.muni.fi.pv168.easyfood.business.service.export.*;
import cz.muni.fi.pv168.easyfood.business.service.validation.DepartmentValidator;
import cz.muni.fi.pv168.easyfood.business.service.validation.EmployeeValidator;
import cz.muni.fi.pv168.easyfood.export.csv.BatchCsvExporter;
import cz.muni.fi.pv168.easyfood.export.csv.BatchCsvImporter;
import cz.muni.fi.pv168.easyfood.storage.sql.DepartmentSqlRepository;
import cz.muni.fi.pv168.easyfood.storage.sql.EmployeeSqlRepository;
import cz.muni.fi.pv168.easyfood.storage.sql.TransactionalImportService;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.DepartmentDao;
import cz.muni.fi.pv168.easyfood.storage.sql.dao.EmployeeDao;
import cz.muni.fi.pv168.easyfood.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.easyfood.storage.sql.db.TransactionConnectionSupplier;
import cz.muni.fi.pv168.easyfood.storage.sql.db.TransactionExecutor;
import cz.muni.fi.pv168.easyfood.storage.sql.db.TransactionExecutorImpl;
import cz.muni.fi.pv168.easyfood.storage.sql.db.TransactionManagerImpl;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper.DepartmentMapper;
import cz.muni.fi.pv168.easyfood.storage.sql.entity.mapper.EmployeeMapper;

import java.util.List;

/**
 * Common dependency provider for both production and test environment.
 */
public class CommonDependencyProvider implements DependencyProvider {

    private final Repository<Department> departments;
    private final Repository<Employee> employees;
    private final DatabaseManager databaseManager;
    private final TransactionExecutor transactionExecutor;
    private final CrudService<Employee> employeeCrudService;
    private final CrudService<Department> departmentCrudService;
    private final ImportService importService;
    private final ExportService exportService;
    private final EmployeeValidator employeeValidator;

    public CommonDependencyProvider(DatabaseManager databaseManager) {
        employeeValidator = new EmployeeValidator();
        var departmentValidator = new DepartmentValidator();
        var guidProvider = new UuidGuidProvider();

        this.databaseManager = databaseManager;
        var transactionManager = new TransactionManagerImpl(databaseManager);
        this.transactionExecutor = new TransactionExecutorImpl(transactionManager::beginTransaction);
        var transactionConnectionSupplier = new TransactionConnectionSupplier(transactionManager, databaseManager);

        var departmentMapper = new DepartmentMapper();
        var departmentDao = new DepartmentDao(transactionConnectionSupplier);

        var employeeMapper = new EmployeeMapper(departmentDao, departmentMapper);

        this.departments = new DepartmentSqlRepository(
                departmentDao,
                departmentMapper
        );
        this.employees = new EmployeeSqlRepository(
                new EmployeeDao(transactionConnectionSupplier),
                employeeMapper
        );
        departmentCrudService = new DepartmentCrudService(departments, departmentValidator, guidProvider);
        employeeCrudService = new EmployeeCrudService(employees, employeeValidator, guidProvider);
        exportService = new GenericExportService(employeeCrudService, departmentCrudService,
                List.of(new BatchCsvExporter()));
        var genericImportService = new GenericImportService(employeeCrudService, departmentCrudService,
                List.of(new BatchCsvImporter()));
        importService = new TransactionalImportService(genericImportService, transactionExecutor);
    }

    @Override
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public Repository<Department> getDepartmentRepository() {
        return departments;
    }

    @Override
    public Repository<Employee> getEmployeeRepository() {
        return employees;
    }

    @Override
    public TransactionExecutor getTransactionExecutor() {
        return transactionExecutor;
    }

    @Override
    public CrudService<Employee> getEmployeeCrudService() {
        return employeeCrudService;
    }

    @Override
    public CrudService<Department> getDepartmentCrudService() {
        return departmentCrudService;
    }

    @Override
    public ImportService getImportService() {
        return importService;
    }

    @Override
    public ExportService getExportService() {
        return exportService;
    }

    @Override
    public EmployeeValidator getEmployeeValidator() {
        return employeeValidator;
    }
}
