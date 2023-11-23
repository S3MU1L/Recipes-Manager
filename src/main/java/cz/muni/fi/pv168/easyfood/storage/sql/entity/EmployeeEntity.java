package cz.muni.fi.pv168.easyfood.storage.sql.entity;

import cz.muni.fi.pv168.easyfood.business.model.Gender;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Representation of Employee entity in a SQL database.
 */
public record EmployeeEntity(
        Long id,
        String guid,
        long departmentId,
        String firstName,
        String lastName,
        Gender gender,
        LocalDate birthDate) {
    public EmployeeEntity(
            Long id,
            String guid,
            long departmentId,
            String firstName,
            String lastName,
            Gender gender,
            LocalDate birthDate) {
        this.id = id;
        this.guid = Objects.requireNonNull(guid, "guid must not be null");
        this.departmentId = departmentId;
        this.firstName = Objects.requireNonNull(firstName, "firstName must not be null");
        this.lastName = Objects.requireNonNull(lastName, "lastName must not be null");
        this.gender = Objects.requireNonNull(gender, "gender must not be null");
        this.birthDate = Objects.requireNonNull(birthDate, "birthDate must not be null");
    }

    public EmployeeEntity(
            String guid,
            long departmentId,
            String firstName,
            String lastName,
            Gender gender,
            LocalDate birthDate) {
        this(null, guid, departmentId, firstName, lastName, gender, birthDate);
    }
}
