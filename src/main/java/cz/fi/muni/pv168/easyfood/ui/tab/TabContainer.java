package cz.fi.muni.pv168.easyfood.ui.tab;

import cz.fi.muni.pv168.easyfood.ui.tablemodel.EntityTableModel;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.IdentityHashMap;
import java.util.Map;

public class TabContainer<E extends EntityTableModel<E>> {

    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final Map<Component, Tab<E>> tabs = new IdentityHashMap<>();

    public void addChangeListener(ChangeListener l) {
        tabbedPane.addChangeListener(l);
    }

    public JComponent getComponent() {
        return tabbedPane;
    }

    public void addTab(Tab<E> tab) {
        JComponent component = tab.getComponent();
        tabbedPane.addTab(tab.getTitle(), component);
        tabs.put(component, tab);
    }

    public Tab<E> getSelectedTab() {
        Component selectedComponent = tabbedPane.getSelectedComponent();
        if (selectedComponent == null) {
            throw new IllegalStateException("No tab is selected");
        }
        Tab<E> tab = tabs.get(selectedComponent);
        if (tab == null) {
            throw new AssertionError("Unknown tabbedPane selectedComponent");
        }
        return tab;
    }
}
