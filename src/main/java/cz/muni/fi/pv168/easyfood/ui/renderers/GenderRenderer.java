package cz.muni.fi.pv168.easyfood.ui.renderers;

import cz.muni.fi.pv168.easyfood.business.model.Gender;
import cz.muni.fi.pv168.easyfood.ui.resources.Icons;

import javax.swing.Icon;
import javax.swing.JLabel;
import java.util.Map;

public final class GenderRenderer extends AbstractRenderer<Gender> {

    private static final Map<Gender, Icon> GENDER_ICONS = Icons.createEnumIcons(Gender.class, 16);

    public GenderRenderer() {
        super(Gender.class);
    }

    @Override
    protected void updateLabel(JLabel label, Gender gender) {
        if (gender != null) {
            label.setIcon(GENDER_ICONS.get(gender));
            label.setText(gender.toString());
        }
    }
}
