package cz.muni.fi.pv168.easyfood.ui.model;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.model.Gender;
import cz.muni.fi.pv168.easyfood.business.service.crud.CrudService;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link javax.swing.table.TableModel} for {@link Employee} objects.
 */
public class EmployeeTableModel extends AbstractTableModel implements EntityTableModel<Employee> {

    private List<Employee> employees;
    private final CrudService<Employee> employeeCrudService;

    private final List<Column<Employee, ?>> columns = List.of(
            Column.editable("Gender", Gender.class, Employee::getGender, Employee::setGender),
            Column.readonly("Age", Integer.class, this::calculateAge),
            Column.editable("Last name", String.class, Employee::getLastName, Employee::setLastName),
            Column.editable("First name", String.class, Employee::getFirstName, Employee::setFirstName),
            Column.readonly("Birthdate", LocalDate.class, Employee::getBirthDate),
            Column.editable("Department", Department.class, Employee::getDepartment, Employee::setDepartment)
    );

    public EmployeeTableModel(CrudService<Employee> employeeCrudService) {
        this.employeeCrudService = employeeCrudService;
        this.employees = new ArrayList<>(employeeCrudService.findAll());
    }

    private int calculateAge(Employee employee) {
        return Period
                .between(employee.getBirthDate(), LocalDate.now())
                .getYears();
    }

    @Override
    public int getRowCount() {
        return employees.size();
    }

    @Override
    public int getColumnCount() {
        return columns.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        var employee = getEntity(rowIndex);
        return columns.get(columnIndex).getValue(employee);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns.get(columnIndex).getName();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columns.get(columnIndex).getColumnType();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columns.get(columnIndex).isEditable();
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (value != null) {
            var employee = getEntity(rowIndex);
            columns.get(columnIndex).setValue(value, employee);
            updateRow(employee);
        }
    }

    public void deleteRow(int rowIndex) {
        var employeeToBeDeleted = getEntity(rowIndex);
        employeeCrudService.deleteByGuid(employeeToBeDeleted.getGuid());
        employees.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(Employee employee) {
        employeeCrudService.create(employee)
                .intoException();
        int newRowIndex = employees.size();
        employees.add(employee);
        fireTableRowsInserted(newRowIndex, newRowIndex);
    }

    public void updateRow(Employee employee) {
        employeeCrudService.update(employee)
                .intoException();
        int rowIndex = employees.indexOf(employee);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public void refresh() {
        this.employees = new ArrayList<>(employeeCrudService.findAll());
        fireTableDataChanged();
    }

    @Override
    public Employee getEntity(int rowIndex) {
        return employees.get(rowIndex);
    }
}
