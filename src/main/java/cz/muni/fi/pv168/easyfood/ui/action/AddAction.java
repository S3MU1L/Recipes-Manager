package cz.muni.fi.pv168.easyfood.ui.action;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.model.Gender;
import cz.muni.fi.pv168.easyfood.business.service.validation.Validator;
import cz.muni.fi.pv168.easyfood.ui.dialog.EmployeeDialog;
import cz.muni.fi.pv168.easyfood.ui.model.EmployeeTableModel;
import cz.muni.fi.pv168.easyfood.ui.resources.Icons;

import javax.swing.AbstractAction;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.util.Objects;

public final class AddAction extends AbstractAction {

    private final JTable employeeTable;
    private final ListModel<Department> departmentListModel;
    private final Validator<Employee> employeeValidator;

    public AddAction(
            JTable employeeTable,
            ListModel<Department> departmentListModel,
            Validator<Employee> employeeValidator) {
        super("Add", Icons.ADD_ICON);
        this.employeeValidator = Objects.requireNonNull(employeeValidator);
        this.employeeTable = employeeTable;
        this.departmentListModel = departmentListModel;
        putValue(SHORT_DESCRIPTION, "Adds new employee");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var employeeTableModel = (EmployeeTableModel) employeeTable.getModel();
        var dialog = new EmployeeDialog(createPrefilledEmployee(), departmentListModel, employeeValidator);
        dialog.show(employeeTable, "Add Employee")
                .ifPresent(employeeTableModel::addRow);
    }

    private Employee createPrefilledEmployee()
    {
        return new Employee(
                "Jan",
                "Novák",
                Gender.MALE,
                LocalDate.now(),
                departmentListModel.getElementAt(0)
        );
    }
}
