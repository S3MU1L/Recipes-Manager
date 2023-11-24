package cz.fi.muni.pv168.easyfood.ui.dialog;

import cz.fi.muni.pv168.easyfood.bussiness.model.Category;
import cz.fi.muni.pv168.easyfood.bussiness.model.Ingredient;
import cz.fi.muni.pv168.easyfood.bussiness.model.Unit;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.List;
import java.util.Optional;

import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;

public abstract class EntityDialog<E> {

    private final JPanel panel = new JPanel();

    public JPanel getPanel() {
        return panel;
    }

    EntityDialog() {
        panel.setLayout(new MigLayout("wrap 2"));
    }

    public void add(String labelText, JComponent component) {
        var label = new JLabel(labelText);
        panel.add(label);
        panel.add(component, "wmin 250lp, grow");
    }

    public abstract E getEntity();
    public abstract boolean valid(E entity);

    public abstract EntityDialog<?> createNewDialog(List<Ingredient> ingredients, List<Category> categories, List<Unit> units);

    public abstract EntityDialog<E> createNewDialog(E entity, List<Ingredient> ingredients, List<Category> categories, List<Unit> units);
    //public abstract void changeEntityInDialog(E entity);

    public Optional<E> show(JComponent parentComponent, String title) {
        do {
            int result = JOptionPane.showOptionDialog(parentComponent, panel, title,
                    OK_CANCEL_OPTION, PLAIN_MESSAGE, null, null, null);
            if (result == OK_OPTION) {
                var entity = getEntity();

                if (entity == null) {
                    return Optional.empty();
                }
                if (!valid(entity)){
                    continue;
                }

                return Optional.of(entity);
            } else {
                return Optional.empty();
            }
        } while (true);

    }
}
