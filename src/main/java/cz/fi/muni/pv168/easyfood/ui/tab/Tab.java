package cz.fi.muni.pv168.easyfood.ui.tab;

import cz.fi.muni.pv168.easyfood.ui.tablemodel.EntityTableModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Tab<E extends EntityTableModel> {
    private JComponent component;
    private String title;
    private JTable table;
    private final E model;

    public Tab(String title, JTable table, E model) {
        this.title = title;
        this.table = table;
        this.model = model;
        this.component = new JScrollPane(table);
    }

    public JComponent getComponent() {
        return component;
    }

    public void setComponent(JComponent component) {
        this.component = component;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public JTable getTable() {
        return table;
    }

    public void setTable(JTable table) {
        this.table = table;
    }

    public E getModel() {
        return model;
    }

    public void delete() {
        int rows[] = table.getSelectedRows();
        for (int i = rows.length - 1; i >= 0; i--) {
            int modelRow = table.convertRowIndexToModel(rows[i]);
            model.deleteRow(modelRow);
        }
    }

}
