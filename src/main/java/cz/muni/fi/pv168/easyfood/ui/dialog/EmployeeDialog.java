package cz.muni.fi.pv168.easyfood.ui.dialog;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.business.model.Gender;
import cz.muni.fi.pv168.easyfood.business.service.validation.Validator;
import cz.muni.fi.pv168.easyfood.ui.model.ComboBoxModelAdapter;
import cz.muni.fi.pv168.easyfood.ui.model.LocalDateModel;
import cz.muni.fi.pv168.easyfood.ui.renderers.DepartmentRenderer;
import cz.muni.fi.pv168.easyfood.ui.renderers.GenderRenderer;
import org.jdatepicker.DateModel;
import org.jdatepicker.JDatePicker;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.ListModel;
import java.time.LocalDate;
import java.util.Objects;

public final class EmployeeDialog extends EntityDialog<Employee> {

    private final JTextField firstNameField = new JTextField();
    private final JTextField lastNameField = new JTextField();
    private final ComboBoxModel<Gender> genderModel = new DefaultComboBoxModel<>(Gender.values());
    private final ComboBoxModel<Department> departmentModel;
    private final DateModel<LocalDate> birthDateModel = new LocalDateModel();

    private final Employee employee;

    public EmployeeDialog(
            Employee employee,
            ListModel<Department> departmentModel,
            Validator<Employee> entityValidator) {
        super(Objects.requireNonNull(entityValidator));
        this.employee = employee;
        this.departmentModel = new ComboBoxModelAdapter<>(departmentModel);
        setValues();
        addFields();
    }

    private void setValues() {
        firstNameField.setText(employee.getFirstName());
        lastNameField.setText(employee.getLastName());
        genderModel.setSelectedItem(employee.getGender());
        departmentModel.setSelectedItem(employee.getDepartment());
        birthDateModel.setValue(employee.getBirthDate());
    }

    private void addFields() {
        var genderComboBox = new JComboBox<>(genderModel);
        genderComboBox.setRenderer(new GenderRenderer());

        var deparmentComboBox = new JComboBox<>(departmentModel);
        deparmentComboBox.setRenderer(new DepartmentRenderer());

        add("First Name:", firstNameField);
        add("Last Name:", lastNameField);
        add("Gender:", genderComboBox);
        add("Birth Date:", new JDatePicker(birthDateModel));
        add("Department:", deparmentComboBox);
        addErrorPanel();
    }

    @Override
    Employee getEntity() {
        employee.setFirstName(firstNameField.getText());
        employee.setLastName(lastNameField.getText());
        employee.setGender((Gender) genderModel.getSelectedItem());
        employee.setDepartment((Department) departmentModel.getSelectedItem());
        employee.setBirthDate(birthDateModel.getValue());
        return employee;
    }
}
