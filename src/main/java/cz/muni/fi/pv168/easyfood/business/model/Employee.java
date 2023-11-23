package cz.muni.fi.pv168.easyfood.business.model;

import java.time.LocalDate;
import java.util.Objects;

public class Employee extends Entity {

    private String firstName;
    private String lastName;
    private Gender gender;
    private LocalDate birthDate;
    private Department department;

    public Employee(
            String guid,
            String firstName,
            String lastName,
            Gender gender,
            LocalDate birthDate,
            Department department) {
        super(guid);
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setBirthDate(birthDate);
        setDepartment(department);
    }

    public Employee(
            String firstName,
            String lastName,
            Gender gender,
            LocalDate birthDate,
            Department department) {
        setFirstName(firstName);
        setLastName(lastName);
        setGender(gender);
        setBirthDate(birthDate);
        setDepartment(department);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = Objects.requireNonNull(firstName, "firstName must not be null");
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = Objects.requireNonNull(lastName, "lastName must not be null");
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = Objects.requireNonNull(gender, "gender must not be null");
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = Objects.requireNonNull(birthDate, "birthDate must not be null");
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = Objects.requireNonNull(department, "department must not be null");
    }

    @Override
    public String toString()
    {
        return "Employee{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", birthDate=" + birthDate +
                ", department=" + department +
                ", guid='" + guid + '\'' +
                '}';
    }
}
