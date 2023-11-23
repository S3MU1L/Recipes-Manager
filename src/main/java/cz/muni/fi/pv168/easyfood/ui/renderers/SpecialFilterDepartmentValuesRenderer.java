package cz.muni.fi.pv168.easyfood.ui.renderers;

import cz.muni.fi.pv168.easyfood.ui.filters.values.SpecialFilterDepartmentValues;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

public class SpecialFilterDepartmentValuesRenderer extends AbstractRenderer<SpecialFilterDepartmentValues> {
    public SpecialFilterDepartmentValuesRenderer() {
        super(SpecialFilterDepartmentValues.class);
    }

    protected void updateLabel(JLabel label, SpecialFilterDepartmentValues value) {
        switch (value) {
            case ALL -> renderAll(label);
            case NO_NERD -> renderNoNerd(label);
        }
    }

    private static void renderAll(JLabel label) {
        label.setText("(ALL)");
        label.setFont(label.getFont().deriveFont(Font.ITALIC));
        label.setForeground(Color.GRAY);
    }

    private static void renderNoNerd(JLabel label) {
        label.setText("NO_NERD");
    }
}
