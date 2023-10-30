package cz.fi.muni.pv168.easyfood.ui.tab;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import java.awt.Component;
import java.util.IdentityHashMap;
import java.util.Map;

public class TabContainer {

    private final JTabbedPane tabbedPane = new JTabbedPane();
    private final Map<Component, Tab> tabs = new IdentityHashMap<>();

    public void addChangeListener(ChangeListener listener) {
        tabbedPane.addChangeListener(listener);
    }

    public JComponent getComponent() {
        return tabbedPane;
    }

    public void addTab(Tab tab) {
        JComponent component = tab.getComponent();
        tabbedPane.addTab(tab.getTitle(), component);
        tabs.put(component, tab);
    }

    public Tab getSelectedTab() {
        Component selectedComponent = tabbedPane.getSelectedComponent();
        if (selectedComponent == null) {
            throw new IllegalStateException("No tab is selected");
        }
        Tab tab = tabs.get(selectedComponent);
        if (tab == null) {
            throw new AssertionError("Unknown tabbedPane selectedComponent");
        }
        return tab;
    }
}
