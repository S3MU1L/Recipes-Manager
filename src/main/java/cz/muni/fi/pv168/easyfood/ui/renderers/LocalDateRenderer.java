package cz.muni.fi.pv168.easyfood.ui.renderers;

import javax.swing.JLabel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class LocalDateRenderer extends AbstractRenderer<LocalDate> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG);

    public LocalDateRenderer() {
        super(LocalDate.class);
    }

    @Override
    protected void updateLabel(JLabel label, LocalDate value) {
        label.setText(formatter.format(value));
    }
}
