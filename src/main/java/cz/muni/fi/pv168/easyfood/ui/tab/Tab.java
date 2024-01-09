package cz.muni.fi.pv168.easyfood.ui.tab;

import cz.muni.fi.pv168.easyfood.ui.dialog.EntityDialog;
import cz.muni.fi.pv168.easyfood.ui.model.tablemodel.EntityTableModel;

import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import java.util.Arrays;

public class Tab<E extends EntityTableModel> {
    private JComponent component;
    private String title;
    private JTable table;
    private final E model;
    private EntityDialog dialog;

    public Tab(String title, JTable table, E model, EntityDialog dialog) {
        this.title = title;
        this.table = table;
        this.model = model;
        this.dialog = dialog;
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

    public EntityDialog getDialog() {
        return dialog;
    }

    public void setDialog(EntityDialog dialog) {
        this.dialog = dialog;
    }

    public void delete() {
        int[] rows = Arrays.stream(table.getSelectedRows()).sequential().map(row -> table.convertRowIndexToModel(row))
                           .toArray();
        model.deleteRows(rows);
    }

}
