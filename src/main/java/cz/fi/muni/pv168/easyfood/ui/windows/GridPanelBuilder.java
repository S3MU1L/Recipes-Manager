package cz.fi.muni.pv168.easyfood.ui.windows;

import javax.swing.*;
import java.awt.*;

public class GridPanelBuilder {

    private final JPanel panel = new JPanel(new GridBagLayout());
    private int nextComponentRow = 0;

    public JPanel getPanel() {
        return panel;
    }

    public void addHeaderRow(String labelText, JComponent component) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = nextComponentRow++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.weightx = 0.0;
        var label = new JLabel(labelText);
        label.setLabelFor(component);
        panel.add(label, c);
        c.gridx = 1;
        c.weightx = 1.0;
        panel.add(component, c);
    }

    public void addBlock(JComponent component) {
        GridBagConstraints c = getBlockConstraints();
        panel.add(component, c);
        ++nextComponentRow;
    }

    public void addFillingBlock(JComponent component) {
        GridBagConstraints c = getBlockConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1.0;
        panel.add(component, c);
        ++nextComponentRow;
    }

    private GridBagConstraints getBlockConstraints() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = nextComponentRow;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.gridx = 0;
        return c;
    }
}
