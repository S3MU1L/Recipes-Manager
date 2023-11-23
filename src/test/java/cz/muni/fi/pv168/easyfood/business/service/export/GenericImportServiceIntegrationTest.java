package cz.muni.fi.pv168.easyfood.business.service.export;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.model.Gender;
import cz.muni.fi.pv168.easyfood.business.model.UuidGuidProvider;
import cz.muni.fi.pv168.easyfood.business.service.crud.DepartmentCrudService;
import cz.muni.fi.pv168.easyfood.business.service.crud.EmployeeCrudService;
import cz.muni.fi.pv168.easyfood.business.service.crud.EntityAlreadyExistsException;
import cz.muni.fi.pv168.easyfood.business.service.validation.DepartmentValidator;
import cz.muni.fi.pv168.easyfood.business.service.validation.EmployeeValidator;
import cz.muni.fi.pv168.easyfood.business.service.validation.ValidationException;
import cz.muni.fi.pv168.easyfood.export.csv.BatchCsvImporter;
import cz.muni.fi.pv168.easyfood.storage.memory.InMemoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Integration tests for the {@link GenericImportService}
 *
 * @author Vojtech Sassmann
 * @since 1.0.0
 */
class GenericImportServiceIntegrationTest
{
    private static final Path PROJECT_ROOT = Paths.get(System.getProperty("project.basedir", "")).toAbsolutePath();
    private static final Path TEST_RESOURCES = PROJECT_ROOT.resolve(Path.of("src", "test", "resources"));

    private EmployeeCrudService employeeCrudService;
    private GenericImportService genericImportService;

    @BeforeEach
    void setUp() {
        var employeeRepository = new InMemoryRepository<Employee>(List.of());
        var employeeValidator = new EmployeeValidator();
        var uuidGuidProvider = new UuidGuidProvider();
        employeeCrudService = new EmployeeCrudService(employeeRepository, employeeValidator, uuidGuidProvider);

        var departmentRepository = new InMemoryRepository<Department>(List.of());
        var departmentValidator = new DepartmentValidator();
        var departmentCrudService = new DepartmentCrudService(departmentRepository, departmentValidator,
                uuidGuidProvider);

        genericImportService = new GenericImportService(
                employeeCrudService,
                departmentCrudService,
                List.of(new BatchCsvImporter())
        );
    }

    @Test
    void importNoEmployees() {
        Path importFilePath = TEST_RESOURCES.resolve("empty.csv");
        genericImportService.importData(importFilePath.toString());

        assertThat(employeeCrudService.findAll())
                .isEmpty();
    }

    @Test
    void singleEmployee() {
        Path importFilePath = TEST_RESOURCES.resolve("single-employee.csv");
        genericImportService.importData(importFilePath.toString());

        // We need to specify the 'usingRecursiveFieldByFieldElementComparator' to actually compare
        // all fields of the Employee and all fields of the Department. If we don't specify this comparator,
        // comparison is done by using the 'equals' method. The 'equals' method compares only the 'guid' fields.
        assertThat(employeeCrudService.findAll())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(
                        new Employee(
                                "76c59af3-6e9a-4fcb-bd7e-d0a163ed8b45",
                                "John",
                                "Doe",
                                Gender.MALE,
                                LocalDate.of(1990, 12, 12),
                                new Department(
                                        "99605972-dc16-4ca1-9fa3-987b7da996f4",
                                        "IT",
                                        "044"
                                )
                        )
                );
    }

    @Test
    void multipleEmployees() {
        Path importFilePath = TEST_RESOURCES.resolve("multiple-employees.csv");
        genericImportService.importData(importFilePath.toString());

        var department = new Department(
                "99605972-dc16-4ca1-9fa3-987b7da996f4",
                "IT",
                "044"
        );
        assertThat(employeeCrudService.findAll())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrder(
                        new Employee(
                                "76c59af3-6e9a-4fcb-bd7e-d0a163ed8b45",
                                "John",
                                "Doe",
                                Gender.MALE,
                                LocalDate.of(1990, 12, 12),
                                department
                        ),
                        new Employee(
                                "dc96a827-b56b-4252-bf24-bb8f25209f3e",
                                "Ellie",
                                "Williams",
                                Gender.FEMALE,
                                LocalDate.of(1992, 6, 12),
                                department
                        )
                );
    }

    @Test
    void invalidFirstNameFails() {
        Path importFilePath = TEST_RESOURCES.resolve("invalid-firstname-employee.csv");

        var stringPath = importFilePath.toString();
        // we want only single invocation in the 'isThrownBy' method so that we don't accidentally catch
        // unwanted exceptions
        assertThatExceptionOfType(ValidationException.class)
                .isThrownBy(() -> genericImportService.importData(stringPath))
                .withMessageContaining("'First name' length is not between 2 (inclusive) and 150 (inclusive)");
    }

    @Test
    void duplicateGuidFails() {
        Path importFilePath = TEST_RESOURCES.resolve("duplicate-guid-employees.csv");

        var stringPath = importFilePath.toString();
        // we want only single invocation in the 'isThrownBy' method so that we don't accidentally catch
        // unwanted exceptions
        assertThatExceptionOfType(EntityAlreadyExistsException.class)
                .isThrownBy(() -> genericImportService.importData(stringPath))
                .withMessage("Employee with given guid already exists: 76c59af3-6e9a-4fcb-bd7e-d0a163ed8b45");
    }
}