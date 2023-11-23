package cz.muni.fi.pv168.easyfood.storage.sql;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.storage.sql.db.DatabaseManager;
import cz.muni.fi.pv168.easyfood.wiring.TestDependencyProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


final class DepartmentRepositoryTest {

    private DatabaseManager databaseManager;
    private Repository<Department> departmentRepository;

    @BeforeEach
    void setUp() {
        databaseManager = DatabaseManager.createTestInstance();
        var dependencyProvider = new TestDependencyProvider(databaseManager);
        departmentRepository = dependencyProvider.getDepartmentRepository();
    }

    @AfterEach
    void tearDown() {
        databaseManager.destroySchema();
    }

    @Test
    void createNewDepartment() {
        Department departmentToCreate = new Department("d-999", "new department", "3333");
        departmentRepository.create(departmentToCreate);

        Department storedDepartment = departmentRepository.findAll().stream()
                .filter(department -> department.getGuid().equals("d-999"))
                .findFirst()
                .orElseThrow();

        assertThat(storedDepartment.getName()).isEqualTo(departmentToCreate.getName());
        assertThat(storedDepartment.getNumber()).isEqualTo(departmentToCreate.getNumber());
    }

    @Test
    void listAllTestingDepartments() {
        List<Department> storedDepartments = departmentRepository
                .findAll();

        assertThat(storedDepartments).hasSize(3);
    }

    @Test
    void updateDepartment() {
        var sales = departmentRepository.findAll()
                .stream()
                .filter(e -> e.getName().equals("Sales"))
                .findFirst()
                .orElseThrow();

        sales.setName("Updated Sales");

        departmentRepository.update(sales);

        Department updatedDepartment = departmentRepository.findByGuid(sales.getGuid()).orElseThrow();

        assertThat(updatedDepartment.getName()).isEqualTo("Updated Sales");
    }
}