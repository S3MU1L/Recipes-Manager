package cz.muni.fi.pv168.easyfood.business.service.export;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.model.Gender;
import cz.muni.fi.pv168.easyfood.business.model.UuidGuidProvider;
import cz.muni.fi.pv168.easyfood.business.service.crud.DepartmentCrudService;
import cz.muni.fi.pv168.easyfood.business.service.crud.EmployeeCrudService;
import cz.muni.fi.pv168.easyfood.business.service.validation.DepartmentValidator;
import cz.muni.fi.pv168.easyfood.business.service.validation.EmployeeValidator;
import cz.muni.fi.pv168.easyfood.export.csv.BatchCsvExporter;
import cz.muni.fi.pv168.easyfood.storage.memory.InMemoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link GenericExportService}
 */
class GenericExportServiceIntegrationTest {

    private static final Path PROJECT_ROOT = Paths.get(System.getProperty("project.basedir", "")).toAbsolutePath();
    private static final Path TEST_RESOURCES = PROJECT_ROOT.resolve(Path.of("src", "test", "resources"));

    private final Path exportFilePath = TEST_RESOURCES
            .resolve("output")
            .resolve(Instant.now().toString().replace(':', '_') + ".csv");

    private GenericExportService genericExportService;
    private EmployeeCrudService employeeCrudService;

    @BeforeEach
    void setUp() {
        var employeeRepository = new InMemoryRepository<Employee>(List.of());
        var employeeValidator = new EmployeeValidator();
        var uuidGuidProvider = new UuidGuidProvider();
        employeeCrudService = new EmployeeCrudService(employeeRepository, employeeValidator, uuidGuidProvider);

        var departmentRepository = new InMemoryRepository<>(setUpDepartments());
        var departmentValidator = new DepartmentValidator();
        var departmentCrudService = new DepartmentCrudService(departmentRepository, departmentValidator,
                uuidGuidProvider);

        genericExportService = new GenericExportService(
                employeeCrudService,
                departmentCrudService,
                List.of(new BatchCsvExporter())
        );
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(exportFilePath)) {
            Files.delete(exportFilePath);
        }
    }

    @Test
    void exportEmpty() throws IOException {
        genericExportService.exportData(exportFilePath.toString());

        assertExportedContent("");
    }

    @Test
    void exportSingleEmployee() throws IOException {
        createEmployees(
                setUpJohnDoe()
        );
        genericExportService.exportData(exportFilePath.toString());

        assertExportedContent(
                """
                76c59af3-6e9a-4fcb-bd7e-d0a163ed8b45,John,Doe,MALE,1990-12-12,99605972-dc16-4ca1-9fa3-987b7da996f4:044:IT
                """
        );
    }

    @Test
    void exportMultipleEmployees() throws IOException {
        createEmployees(
                setUpJohnDoe(),
                setUpEllieWilliams()
        );
        genericExportService.exportData(exportFilePath.toString());

        assertExportedContent(
        """
                76c59af3-6e9a-4fcb-bd7e-d0a163ed8b45,John,Doe,MALE,1990-12-12,99605972-dc16-4ca1-9fa3-987b7da996f4:044:IT
                dc96a827-b56b-4252-bf24-bb8f25209f3e,Ellie,Williams,FEMALE,1992-06-12,99605972-dc16-4ca1-9fa3-987b7da996f4:044:IT
                """
        );
    }

    private void assertExportedContent(String expectedContent) throws IOException {
        assertThat(Files.readString(exportFilePath))
                .isEqualToIgnoringNewLines(expectedContent);
    }

    private Department setUpDepartment() {
        return new Department("99605972-dc16-4ca1-9fa3-987b7da996f4", "IT", "044");
    }

    private List<Department> setUpDepartments() {
        return List.of(setUpDepartment());
    }

    private void createEmployees(Employee... employees) {
        for (Employee employee : employees)
        {
            employeeCrudService.create(employee);
        }
    }

    private Employee setUpJohnDoe() {
        return new Employee("76c59af3-6e9a-4fcb-bd7e-d0a163ed8b45", "John", "Doe", Gender.MALE,
                LocalDate.of(1990, 12, 12), setUpDepartment());
    }

    private Employee setUpEllieWilliams() {
        return new Employee("dc96a827-b56b-4252-bf24-bb8f25209f3e", "Ellie", "Williams",
                Gender.FEMALE, LocalDate.of(1992, 6, 12), setUpDepartment());
    }
}