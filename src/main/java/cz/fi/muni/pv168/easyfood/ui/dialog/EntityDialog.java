package cz.fi.muni.pv168.easyfood.ui.dialog;

import net.miginfocom.swing.MigLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.html.parser.Entity;
import java.util.Optional;

import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;

public abstract class EntityDialog<E> {

    private final JPanel panel = new JPanel();

    EntityDialog() {
        panel.setLayout(new MigLayout("wrap 2"));
    }

    public void add(String labelText, JComponent component) {
        var label = new JLabel(labelText);
        panel.add(label);
        panel.add(component, "wmin 250lp, grow");
    }

    public abstract E getEntity();

    public abstract EntityDialog<?> createNewDialog();
    public abstract EntityDialog<E> createNewDialog(E entity);

    public Optional<E> show(JComponent parentComponent, String title) {
        int result = JOptionPane.showOptionDialog(parentComponent, panel, title,
                OK_CANCEL_OPTION, PLAIN_MESSAGE, null, null, null);
        if (result == OK_OPTION) {
            return Optional.of(getEntity());
        } else {
            return Optional.empty();
        }
    }
}
