package cz.muni.fi.pv168.easyfood.ui;

import cz.muni.fi.pv168.easyfood.business.model.Department;
import cz.muni.fi.pv168.easyfood.business.model.Gender;
import cz.muni.fi.pv168.easyfood.ui.action.AddAction;
import cz.muni.fi.pv168.easyfood.ui.action.DeleteAction;
import cz.muni.fi.pv168.easyfood.ui.action.EditAction;
import cz.muni.fi.pv168.easyfood.ui.action.ExportAction;
import cz.muni.fi.pv168.easyfood.ui.action.ImportAction;
import cz.muni.fi.pv168.easyfood.ui.action.NuclearQuitAction;
import cz.muni.fi.pv168.easyfood.ui.action.QuitAction;
import cz.muni.fi.pv168.easyfood.ui.filters.EmployeeTableFilter;
import cz.muni.fi.pv168.easyfood.ui.filters.components.FilterComboboxBuilder;
import cz.muni.fi.pv168.easyfood.ui.filters.components.FilterListModelBuilder;
import cz.muni.fi.pv168.easyfood.ui.filters.values.SpecialFilterDepartmentValues;
import cz.muni.fi.pv168.easyfood.ui.filters.values.SpecialFilterGenderValues;
import cz.muni.fi.pv168.easyfood.ui.model.DepartmentListModel;
import cz.muni.fi.pv168.easyfood.ui.model.EmployeeTableModel;
import cz.muni.fi.pv168.easyfood.ui.model.EntityListModelAdapter;
import cz.muni.fi.pv168.easyfood.ui.panels.EmployeeListPanel;
import cz.muni.fi.pv168.easyfood.ui.panels.EmployeeTablePanel;
import cz.muni.fi.pv168.easyfood.ui.renderers.DepartmentRenderer;
import cz.muni.fi.pv168.easyfood.ui.renderers.GenderRenderer;
import cz.muni.fi.pv168.easyfood.ui.renderers.SpecialFilterDepartmentValuesRenderer;
import cz.muni.fi.pv168.easyfood.ui.renderers.SpecialFilterGenderValuesRenderer;
import cz.muni.fi.pv168.easyfood.util.Either;
import cz.muni.fi.pv168.easyfood.wiring.DependencyProvider;

import javax.swing.Action;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.table.TableRowSorter;
import java.awt.BorderLayout;
import java.awt.Component;

public class MainWindow {

    private final JFrame frame;
    private final Action quitAction = new QuitAction();
    private final Action nuclearQuit;
    private final Action addAction;
    private final Action deleteAction;
    private final Action editAction;
    private final Action exportAction;
    private final Action importAction;
    private final EmployeeTableModel employeeTableModel;
    private final DepartmentListModel departmentListModel;

    public MainWindow(DependencyProvider dependencyProvider) {
        frame = createFrame();

        employeeTableModel = new EmployeeTableModel(dependencyProvider.getEmployeeCrudService());
        departmentListModel = new DepartmentListModel(dependencyProvider.getDepartmentCrudService());
        var employeeListModel = new EntityListModelAdapter<>(employeeTableModel);

        var employeeTablePanel = new EmployeeTablePanel(employeeTableModel, departmentListModel, this::changeActionsState);
        var employeeListPanel = new EmployeeListPanel(employeeListModel);

        nuclearQuit = new NuclearQuitAction(dependencyProvider.getDatabaseManager());
        addAction = new AddAction(employeeTablePanel.getTable(), departmentListModel,
                dependencyProvider.getEmployeeValidator());
        deleteAction = new DeleteAction(employeeTablePanel.getTable());
        editAction = new EditAction(employeeTablePanel.getTable(), departmentListModel,
                dependencyProvider.getEmployeeValidator());
        exportAction = new ExportAction(employeeTablePanel, dependencyProvider.getExportService());
        importAction = new ImportAction(employeeTablePanel, dependencyProvider.getImportService(), this::refresh);

        employeeTablePanel.setComponentPopupMenu(createEmployeeTablePopupMenu());

        var tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Employees (table)", employeeTablePanel);
        tabbedPane.addTab("Employees (list)", employeeListPanel);

        frame.add(tabbedPane, BorderLayout.CENTER);

        var rowSorter = new TableRowSorter<>(employeeTableModel);
        var employeeTableFilter = new EmployeeTableFilter(rowSorter);
        employeeTablePanel.getTable().setRowSorter(rowSorter);

        var genderFilter = createGenderFilter(employeeTableFilter);
        var departmentFilter = new JScrollPane(createDepartmentFilter(employeeTableFilter, departmentListModel));

        frame.add(createToolbar(genderFilter, departmentFilter), BorderLayout.BEFORE_FIRST_LINE);
        frame.setJMenuBar(createMenuBar());
        frame.pack();
        changeActionsState(0);
    }

    private void refresh() {
        departmentListModel.refresh();
        employeeTableModel.refresh();
    }

    private static JList<Either<SpecialFilterDepartmentValues, Department>> createDepartmentFilter(
            EmployeeTableFilter employeeTableFilter, DepartmentListModel departmentListModel) {
        return FilterListModelBuilder.create(SpecialFilterDepartmentValues.class, departmentListModel)
                .setSelectedIndex(0)
                .setVisibleRowsCount(3)
                .setSpecialValuesRenderer(new SpecialFilterDepartmentValuesRenderer())
                .setValuesRenderer(new DepartmentRenderer())
                .setFilter(employeeTableFilter::filterDepartment)
                .build();
    }

    private static JComboBox<Either<SpecialFilterGenderValues, Gender>> createGenderFilter(
            EmployeeTableFilter employeeTableFilter) {
        return FilterComboboxBuilder.create(SpecialFilterGenderValues.class, Gender.values())
                .setSelectedItem(SpecialFilterGenderValues.BOTH)
                .setSpecialValuesRenderer(new SpecialFilterGenderValuesRenderer())
                .setValuesRenderer(new GenderRenderer())
                .setFilter(employeeTableFilter::filterGender)
                .build();
    }

    public void show() {
        frame.setVisible(true);
    }

    private JFrame createFrame() {
        var frame = new JFrame("Employee records");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        return frame;
    }

    private JPopupMenu createEmployeeTablePopupMenu() {
        var menu = new JPopupMenu();
        menu.add(deleteAction);
        menu.add(editAction);
        menu.add(addAction);
        return menu;
    }

    private JMenuBar createMenuBar() {
        var menuBar = new JMenuBar();
        var editMenu = new JMenu("Edit");
        editMenu.setMnemonic('e');
        editMenu.add(addAction);
        editMenu.add(editAction);
        editMenu.add(deleteAction);
        editMenu.addSeparator();
        editMenu.add(quitAction);
        menuBar.add(editMenu);
        editMenu.add(nuclearQuit);
        return menuBar;
    }

    private JToolBar createToolbar(Component... components) {
        var toolbar = new JToolBar();
        toolbar.add(quitAction);
        toolbar.addSeparator();
        toolbar.add(addAction);
        toolbar.add(editAction);
        toolbar.add(deleteAction);
        toolbar.addSeparator();
        toolbar.add(exportAction);
        toolbar.add(importAction);

        for (var component : components) {
            toolbar.add(component);
        }

        return toolbar;
    }

    private void changeActionsState(int selectedItemsCount) {
        editAction.setEnabled(selectedItemsCount == 1);
        deleteAction.setEnabled(selectedItemsCount >= 1);
    }
}
