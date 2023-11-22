package cz.fi.muni.pv168.easyfood.ui.table;

import cz.fi.muni.pv168.easyfood.ui.table.tablemodel.ServiceTableModel;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import java.awt.event.MouseListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public abstract class Table<E> {
    protected final JTable table;
    private final ServiceTableModel<E> tableModel;

    public Table(ServiceTableModel<E> tableModel, List<Action> editActions) {
        this.tableModel = tableModel;
        this.table = createTable(tableModel);
        this.table.setComponentPopupMenu(createTablePopupMenu(editActions));
    }

    public Table(ServiceTableModel<E> tableModel) {
        this(tableModel, Collections.emptyList());
    }

    public JScrollPane wrapIntoScrollPane() {
        return new JScrollPane(table);
    }

    public void add() {
        tableModel.addRow();
    }

    public void update(int rowIndex) {
        tableModel.updateRow(rowIndex);
    }

    public void updateAllRows() {
        tableModel.updateAllRows();
    }

    public void delete(int rowIndex) {
        tableModel.deleteRow(rowIndex);
    }

    public void delete(E entity) {
        tableModel.deleteRow(tableModel.getRowIndex(entity));
    }

    public void addListSelectionListener(ListSelectionListener listSelectionListener) {
        table.getSelectionModel().addListSelectionListener(listSelectionListener);
    }

    public void addMouseListener(MouseListener listener) {
        table.addMouseListener(listener);
    }

    public void addKeyListener(KeyStroke keyStroke, Action action) {
        InputMap im = table.getInputMap();
        Object enterActionKey = new Object();
        im.put(keyStroke, enterActionKey);
        ActionMap am = table.getActionMap();
        am.put(enterActionKey, action);
    }

    public void addActions(List<Action> actions) {
        actions.forEach(table.getComponentPopupMenu()::add);
    }

    public int getSelectedRow() {
        return table.convertRowIndexToModel(table.getSelectedRow());
    }

    public List<Integer> getSelectedRows() {
        return Arrays.stream(table.getSelectedRows())
                .map(table::convertRowIndexToModel)
                .boxed()
                .collect(Collectors.toList());
    }

    public E getSelectedEntity() {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length != 1) {
            throw new IllegalStateException("Invalid selected rows count (must be 1): " + Arrays.toString(selectedRows));
        }
        return tableModel.getEntity(table.convertRowIndexToModel(selectedRows[0]));
    }

    public List<E> getSelectedEntities() {
        return getSelectedRows().stream().map(tableModel::getEntity).collect(Collectors.toList());
    }

    public int getSelectedCount() {
        return table.getSelectedRowCount();
    }

    public int getRowsCount() {
        return tableModel.getRowCount();
    }

    public void clearSelection() {
        table.getSelectionModel().clearSelection();
    }

    private JTable createTable(TableModel tableModel) {
        var table = new JTable(tableModel);
        table.setAutoCreateRowSorter(true);
        table.setRowHeight(20);
        return table;
    }

    private JPopupMenu createTablePopupMenu(List<Action> editActions) {
        var menu = new JPopupMenu();
        editActions.forEach(menu::add);
        return menu;
    }
}
