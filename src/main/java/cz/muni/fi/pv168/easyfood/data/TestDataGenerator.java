package cz.muni.fi.pv168.easyfood.data;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.model.Gender;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

import static java.time.Month.DECEMBER;
import static java.time.Month.JANUARY;
import static java.time.temporal.ChronoUnit.DAYS;

public final class TestDataGenerator {

    private static final Map<Gender, List<String>> FIRST_NAMES = Map.of(
            Gender.MALE, List.of("Jiří", "Jan", "Petr", "Josef", "Pavel", "Martin", "Tomáš", "Jaroslav", "Miroslav", "Zdeněk"),
            Gender.FEMALE, List.of("Jana", "Marie", "Eva", "Hana", "Anna", "Lenka", "Kateřina", "Lucie", "Věra", "Alena")
    );

    private static final Map<Gender, List<String>> LAST_NAMES = Map.of(
            Gender.MALE, List.of("Novák", "Novotný", "Dvořák", "Černý", "Procházka", "Šťastný", "Veselý", "Horák", "Němec", "Pokorný"),
            Gender.FEMALE, List.of("Nováková", "Novotná", "Dvořáková", "Černá", "Procházková", "Šťastná", "Veselá", "Horáková", "Němcová", "Pokorná")
    );

    private static final List<Department> DEPARTMENTS = List.of(
            new Department(UUID.randomUUID().toString(), "IT", "007"),
            new Department(UUID.randomUUID().toString(), "Sales", "666"),
            new Department(UUID.randomUUID().toString(), "HR", "112")
    );

    private static final LocalDate MIN_BIRTH_DATE = LocalDate.of(1950, JANUARY, 1);
    private static final LocalDate MAX_BIRTH_DATE = LocalDate.of(2002, DECEMBER, 31);

    private final Random random = new Random(2L);

    public Employee createTestEmployee() {
        Gender gender = selectRandom(Arrays.asList(Gender.values()));
        String firstName = selectRandom(FIRST_NAMES.get(gender));
        String lastName = selectRandom(LAST_NAMES.get(gender));
        LocalDate birthDate = selectRandomLocalDate(MIN_BIRTH_DATE, MAX_BIRTH_DATE);
        Department department = selectRandom(DEPARTMENTS);
        return new Employee(UUID.randomUUID().toString(), firstName, lastName, gender, birthDate, department);
    }

    public List<Employee> createTestEmployees(int count) {
        return Stream
                .generate(this::createTestEmployee)
                .limit(count)
                .toList();
    }

    public List<Department> getDepartments() {
        return DEPARTMENTS;
    }

    private <T> T selectRandom(List<T> data) {
        int index = random.nextInt(data.size());
        return data.get(index);
    }

    private LocalDate selectRandomLocalDate(LocalDate min, LocalDate max) {
        int maxDays = Math.toIntExact(DAYS.between(min, max) + 1);
        int days = random.nextInt(maxDays);
        return min.plusDays(days);
    }
}
