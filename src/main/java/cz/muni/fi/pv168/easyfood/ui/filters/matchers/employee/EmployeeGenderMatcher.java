package cz.muni.fi.pv168.easyfood.ui.filters.matchers.employee;

import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.model.Gender;
import cz.muni.fi.pv168.easyfood.ui.filters.matchers.EntityMatcher;

public class EmployeeGenderMatcher extends EntityMatcher<Employee> {
    private final Gender gender;

    public EmployeeGenderMatcher(Gender gender) {
        this.gender = gender;
    }

    @Override
    public boolean evaluate(Employee employee) {
        return employee.getGender() == gender;
    }
}