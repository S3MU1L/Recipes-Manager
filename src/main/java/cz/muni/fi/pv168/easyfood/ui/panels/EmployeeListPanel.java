package cz.muni.fi.pv168.easyfood.ui.panels;

import cz.muni.fi.pv168.easyfood.business.model.Employee;
import cz.muni.fi.pv168.easyfood.ui.renderers.EmployeeRenderer;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import java.awt.BorderLayout;

/**
 * Panel with employee records in a list.
 */
public class EmployeeListPanel extends JPanel {

    public EmployeeListPanel(ListModel<Employee> employeeListModel) {
        var list = new JList<>(employeeListModel);
        list.setCellRenderer(new EmployeeRenderer());
        setLayout(new BorderLayout());
        add(new JScrollPane(list));
    }
}
