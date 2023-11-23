package cz.muni.fi.pv168.easyfood.ui.renderers;

import cz.muni.fi.pv168.easyfood.business.model.Department;

import javax.swing.JLabel;

public class DepartmentRenderer extends AbstractRenderer<Department> {

    public DepartmentRenderer() {
        super(Department.class);
    }

    @Override
    protected void updateLabel(JLabel label, Department value) {
        label.setText(String.format("%s: %s", value.getNumber(), value.getName()));
    }
}