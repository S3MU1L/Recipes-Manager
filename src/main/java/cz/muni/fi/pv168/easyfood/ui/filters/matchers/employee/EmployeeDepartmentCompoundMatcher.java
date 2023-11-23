package cz.muni.fi.pv168.easyfood.ui.filters.matchers.employee;

import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.ui.filters.matchers.EntityMatcher;

import java.util.Collection;

public class EmployeeDepartmentCompoundMatcher extends EntityMatcher<Employee> {

    private final Collection<EntityMatcher<Employee>> employeeMatchers;

    public EmployeeDepartmentCompoundMatcher(Collection<EntityMatcher<Employee>> employeeMatchers) {
        this.employeeMatchers = employeeMatchers;
    }

    @Override
    public boolean evaluate(Employee employee) {
        return employeeMatchers.stream()
                .anyMatch(matcher -> matcher.evaluate(employee));
    }
}
