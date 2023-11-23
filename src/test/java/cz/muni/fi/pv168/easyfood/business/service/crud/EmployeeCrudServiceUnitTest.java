package cz.muni.fi.pv168.easyfood.business.service.crud;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.model.Gender;
import cz.muni.fi.pv168.easyfood.business.model.GuidProvider;
import cz.muni.fi.pv168.easyfood.business.repository.Repository;
import cz.muni.fi.pv168.easyfood.business.service.validation.EmployeeValidator;
import cz.muni.fi.pv168.easyfood.business.service.validation.ValidationResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link EmployeeCrudService}
 */
class EmployeeCrudServiceUnitTest {
    private static final Department IT_DEPARTMENT = new Department("d-1", "IT", "22");

    private EmployeeCrudService employeeCrudService;
    private Repository<Employee> employeeRepository;
    private EmployeeValidator employeeValidator;
    private GuidProvider guidProvider;


    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        employeeRepository = Mockito.mock(Repository.class);
        employeeValidator = Mockito.mock(EmployeeValidator.class);
        guidProvider = Mockito.mock(GuidProvider.class);
        employeeCrudService = new EmployeeCrudService(employeeRepository, employeeValidator, guidProvider);
    }

    @Test
    void createWithGuidSucceeds() {
        var employee = createEmployeeInstance("e-1");

        when(employeeValidator.validate(employee))
                .thenReturn(ValidationResult.success());

        var result = employeeCrudService.create(employee);

        assertThat(result).isEqualTo(ValidationResult.success());
        verify(employeeRepository, times(1))
                .create(employee);
    }

    @Test
    void createWithoutGuidSucceeds() {
        var employee = createEmployeeInstance(null);
        var newGuid = "new-guid";
        var expectedEmployee = createEmployeeInstance(newGuid);

        when(guidProvider.newGuid())
                .thenReturn(newGuid);

        when(employeeValidator.validate(employee))
                .thenReturn(ValidationResult.success());

        var result = employeeCrudService.create(employee);

        assertThat(result).isEqualTo(ValidationResult.success());
        verify(employeeRepository, times(1))
                .create(refEq(expectedEmployee));
        // The 'refEq' is used to compare instances field by field. Default compares instances
        // using the 'equals' method. That is not sufficient because we want to test here that
        // all fields have the exact value we expect.
    }

    @Test
    void createValidationError() {
        var employee = createEmployeeInstance("e-1");

        when(employeeValidator.validate(employee))
                .thenReturn(ValidationResult.failed("validation failed"));

        var result = employeeCrudService.create(employee);

        assertThat(result).isEqualTo(ValidationResult.failed("validation failed"));
        verify(employeeRepository, times(0))
                .create(employee);
    }

    @Test
    void createFailsForDuplicateGuid() {
        var employee = createEmployeeInstance("e-1");

        when(employeeValidator.validate(employee))
                .thenReturn(ValidationResult.success());
        when(employeeRepository.existsByGuid("e-1"))
                .thenReturn(true);

        assertThatExceptionOfType(EntityAlreadyExistsException.class)
                .isThrownBy(() -> employeeCrudService.create(employee))
                .withMessage("Employee with given guid already exists: e-1");
    }

    @Test
    void updateWithGuidSucceeds() {
        var employee = createEmployeeInstance("e-1");

        when(employeeValidator.validate(employee))
                .thenReturn(ValidationResult.success());

        var result = employeeCrudService.update(employee);

        assertThat(result).isEqualTo(ValidationResult.success());
        verify(employeeRepository, times(1))
                .update(employee);
    }

    @Test
    void updateValidationError() {
        var employee = createEmployeeInstance("e-1");

        when(employeeValidator.validate(employee))
                .thenReturn(ValidationResult.failed("validation failed"));

        var result = employeeCrudService.update(employee);

        assertThat(result).isEqualTo(ValidationResult.failed("validation failed"));
        verify(employeeRepository, times(0))
                .update(employee);
    }

    @Test
    void deleteByGuid() {
        employeeCrudService.deleteByGuid("guid");

        verify(employeeRepository, times(1))
                .deleteByGuid("guid");
    }

    @Test
    void findAll() {
        var expectedEmployeeList = List.of(createEmployeeInstance("e-1"));
        when(employeeRepository.findAll())
                .thenReturn(expectedEmployeeList);

        var foundEmployees = employeeCrudService.findAll();

        assertThat(foundEmployees).isEqualTo(expectedEmployeeList);
    }

    @Test
    void deleteAll() {
        employeeCrudService.deleteAll();

        verify(employeeRepository, times(1))
                .deleteAll();
    }

    private static Employee createEmployeeInstance(String guid) {
        return new Employee(
                guid,
                "expected",
                "employee",
                Gender.MALE,
                LocalDate.EPOCH,
                IT_DEPARTMENT
        );
    }
}