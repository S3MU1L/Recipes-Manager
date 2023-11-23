package cz.muni.fi.pv168.easyfood.ui.filters.matchers.employee;

import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.ui.filters.matchers.EntityMatcher;

public class EmployeeDepartmentNoNerdMatcher extends EntityMatcher<Employee> {

    @Override
    public boolean evaluate(Employee employee) {
        return !"IT".equals(employee.getDepartment().getName());
    }
}
