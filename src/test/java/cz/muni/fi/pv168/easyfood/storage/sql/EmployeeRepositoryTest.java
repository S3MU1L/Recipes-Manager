package cz.muni.fi.pv168.easyfood.storage.sql;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.model.Gender;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.easyfood.wiring.TestDependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for Employee repository.
 */
final class EmployeeRepositoryTest {

    private DatabaseManager databaseManager;
    private Repository<Employee> employeeRepository;
    private Repository<Department> departmentRepository;

    @BeforeEach
    void setUp() {
        databaseManager = DatabaseManager.createTestInstance();
        var dependencyProvider = new TestDependencyProvider(databaseManager);
        employeeRepository = dependencyProvider.getEmployeeRepository();
        departmentRepository = dependencyProvider.getDepartmentRepository();
    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    @Test
    void createNewEmployee() {
        var department = findSalesDepartment();

        var employeeToCreate = new Employee(
                "e-999",
                "John",
                "Doe",
                Gender.MALE,
                LocalDate.of(2000, 1, 1),
                department
        );

        employeeRepository.create(employeeToCreate);

        var employeeOptional = employeeRepository.findByGuid("e-999");

        assertThat(employeeOptional).isPresent();
        assertThat(employeeOptional.get())
                .usingRecursiveComparison()
                .isEqualTo(employeeToCreate);
    }

    @Test
    void updateEmployee() {
        var employee = employeeRepository.findByGuid("e1")
                .orElseThrow();

        employee.setFirstName("Jane");
        employee.setLastName("Doe");
        employee.setGender(Gender.FEMALE);

        employeeRepository.update(employee);

        var updatedEmployee = employeeRepository.findByGuid("e1")
                .orElseThrow();

        assertThat(updatedEmployee.getFirstName()).isEqualTo("Jane");
        assertThat(updatedEmployee.getLastName()).isEqualTo("Doe");
        assertThat(updatedEmployee.getGender()).isEqualTo(Gender.FEMALE);
    }

    private Department findSalesDepartment() {
        return departmentRepository.findAll().stream()
                .filter(e -> e.getName().equals("Sales"))
                .findFirst()
                .orElseThrow();
    }
}
