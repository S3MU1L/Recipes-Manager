package cz.fi.muni.pv168.easyfood.ui.tab;

import javax.swing.*;

public class Tab<E> {
    private JComponent component;
    private String title;
    private JTable table;

    public Tab(String title, JTable table) {
        this.title = title;
        this.table = table;
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

}
