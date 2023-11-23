package cz.muni.fi.pv168.easyfood.ui.panels;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Gender;
import cz.muni.fi.pv168.easyfood.ui.model.ComboBoxModelAdapter;
import cz.muni.fi.pv168.easyfood.ui.model.DepartmentListModel;
import cz.muni.fi.pv168.easyfood.ui.model.EmployeeTableModel;
import cz.muni.fi.pv168.easyfood.ui.renderers.DepartmentRenderer;
import cz.muni.fi.pv168.easyfood.ui.renderers.GenderRenderer;
import cz.muni.fi.pv168.easyfood.ui.renderers.LocalDateRenderer;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import java.awt.BorderLayout;
import java.time.LocalDate;
import java.util.function.Consumer;

/**
 * Panel with employee records in a table.
 */
public class EmployeeTablePanel extends JPanel {

    private final JTable table;
    private final Consumer<Integer> onSelectionChange;
    private final EmployeeTableModel employeeTableModel;

    public EmployeeTablePanel(EmployeeTableModel employeeTableModel, DepartmentListModel departmentListModel, Consumer<Integer> onSelectionChange) {
        setLayout(new BorderLayout());
        table = setUpTable(employeeTableModel, departmentListModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        this.onSelectionChange = onSelectionChange;
        this.employeeTableModel = employeeTableModel;
    }

    public JTable getTable() {
        return table;
    }

    private JTable setUpTable(EmployeeTableModel employeeTableModel, DepartmentListModel departmentListModel) {
        var table = new JTable(employeeTableModel);

        var genderRenderer = new GenderRenderer();
        var departmentRenderer = new DepartmentRenderer();

        table.setAutoCreateRowSorter(true);
        table.getSelectionModel().addListSelectionListener(this::rowSelectionChanged);

        var genderComboBox = new JComboBox<>(Gender.values());
        genderComboBox.setRenderer(genderRenderer);
        table.setDefaultEditor(Gender.class, new DefaultCellEditor(genderComboBox));

        var departmentComboBox = new JComboBox<>(new ComboBoxModelAdapter<>(departmentListModel));
        departmentComboBox.setRenderer(departmentRenderer);
        table.setDefaultEditor(Department.class, new DefaultCellEditor(departmentComboBox));

        table.setDefaultRenderer(Gender.class, genderRenderer);
        table.setDefaultRenderer(Department.class, departmentRenderer);
        table.setDefaultRenderer(LocalDate.class, new LocalDateRenderer());

        return table;
    }

    private void rowSelectionChanged(ListSelectionEvent listSelectionEvent) {
        var selectionModel = (ListSelectionModel) listSelectionEvent.getSource();
        var count = selectionModel.getSelectedItemsCount();
        if (onSelectionChange != null) {
            onSelectionChange.accept(count);
        }
    }

    public void refresh() {
        employeeTableModel.refresh();
    }
}
