package cz.muni.fi.pv168.easyfood.ui.renderers;

import cz.muni.fi.pv168.easyfood.ui.filters.values.SpecialFilterGenderValues;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;

public class SpecialFilterGenderValuesRenderer extends AbstractRenderer<SpecialFilterGenderValues> {

    public SpecialFilterGenderValuesRenderer() {
        super(SpecialFilterGenderValues.class);
    }

    protected void updateLabel(JLabel label, SpecialFilterGenderValues value) {
        switch (value) {
            case BOTH -> renderBoth(label);
        }
    }

    private static void renderBoth(JLabel label) {
        label.setText("(BOTH)");
        label.setFont(label.getFont().deriveFont(Font.ITALIC));
        label.setForeground(Color.GRAY);
    }
}
