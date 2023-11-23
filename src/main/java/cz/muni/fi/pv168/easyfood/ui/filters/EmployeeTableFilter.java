package cz.muni.fi.pv168.easyfood.ui.filters;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.model.Gender;
import cz.muni.fi.pv168.easyfood.ui.filters.matchers.EntityMatcher;
import cz.muni.fi.pv168.easyfood.ui.filters.matchers.EntityMatchers;
import cz.muni.fi.pv168.easyfood.ui.filters.matchers.employee.EmployeeDepartmentCompoundMatcher;
import cz.muni.fi.pv168.easyfood.ui.filters.matchers.employee.EmployeeDepartmentMatcher;
import cz.muni.fi.pv168.easyfood.ui.filters.matchers.employee.EmployeeGenderMatcher;
import cz.muni.fi.pv168.easyfood.ui.filters.values.SpecialFilterDepartmentValues;
import cz.muni.fi.pv168.easyfood.ui.filters.values.SpecialFilterGenderValues;
import cz.muni.fi.pv168.easyfood.ui.model.EmployeeTableModel;
import cz.muni.fi.pv168.easyfood.util.Either;

import javax.swing.table.TableRowSorter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Class holding all filters for the EmployeeTable.
 */
public final class EmployeeTableFilter {
    private final EmployeeCompoundMatcher employeeCompoundMatcher;

    public EmployeeTableFilter(TableRowSorter<EmployeeTableModel> rowSorter) {
        employeeCompoundMatcher = new EmployeeCompoundMatcher(rowSorter);
        rowSorter.setRowFilter(employeeCompoundMatcher);
    }

    public void filterGender(Either<SpecialFilterGenderValues, Gender> selectedItem) {
        selectedItem.apply(
                l -> employeeCompoundMatcher.setGenderMatcher(l.getMatcher()),
                r -> employeeCompoundMatcher.setGenderMatcher(new EmployeeGenderMatcher(r))
        );
    }

    public void filterDepartment(List<Either<SpecialFilterDepartmentValues, Department>> selectedItems) {
        List<EntityMatcher<Employee>> matchers = new ArrayList<>();
        selectedItems.forEach(either -> either.apply(
                l -> matchers.add(l.getMatcher()),
                r -> matchers.add(new EmployeeDepartmentMatcher(r))
        ));
        employeeCompoundMatcher.setDepartmentMatcher(new EmployeeDepartmentCompoundMatcher(matchers));
    }

    /**
     * Container class for all matchers for the EmployeeTable.
     *
     * This Matcher evaluates to true, if all contained {@link EntityMatcher} instances
     * evaluate to true.
     */
    private static class EmployeeCompoundMatcher extends EntityMatcher<Employee> {

        private final TableRowSorter<EmployeeTableModel> rowSorter;
        private EntityMatcher<Employee> genderMatcher = EntityMatchers.all();
        private EntityMatcher<Employee> departmentMatcher = EntityMatchers.all();

        private EmployeeCompoundMatcher(TableRowSorter<EmployeeTableModel> rowSorter) {
            this.rowSorter = rowSorter;
        }

        private void setGenderMatcher(EntityMatcher<Employee> genderMatcher) {
            this.genderMatcher = genderMatcher;
            rowSorter.sort();
        }

        private void setDepartmentMatcher(EntityMatcher<Employee> departmentMatcher) {
            this.departmentMatcher = departmentMatcher;
            rowSorter.sort();
        }

        @Override
        public boolean evaluate(Employee employee) {
            return Stream.of(genderMatcher, departmentMatcher)
                    .allMatch(m -> m.evaluate(employee));
        }
    }
}
