package cz.muni.fi.pv168.easyfood.ui.dialog;

import cz.muni.fi.pv168.easyfood.business.service.validation.Validator;
import net.miginfocom.swing.MigLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static javax.swing.JOptionPane.OK_CANCEL_OPTION;
import static javax.swing.JOptionPane.OK_OPTION;
import static javax.swing.JOptionPane.PLAIN_MESSAGE;

abstract class EntityDialog<E> {

    private final JPanel panel = new JPanel();
    private final JPanel errors = new JPanel();
    private final Validator<E> entityValidator;

    EntityDialog(Validator<E> entityValidator) {
        this.entityValidator = Objects.requireNonNull(entityValidator);
        panel.setLayout(new MigLayout("wrap 2"));
        errors.setLayout(new MigLayout("wrap 1"));
    }

    protected void addErrorPanel() {
        panel.add(errors, "span");
    }

    void add(String labelText, JComponent component) {
        var label = new JLabel(labelText);
        panel.add(label);
        panel.add(component, "wmin 250lp, grow");
    }

    abstract E getEntity();

    private void showErrorMessages(List<String> messages) {
        errors.removeAll();
        messages.stream().map(JLabel::new).forEach(errors::add);
    }

    private int showOptionDialog(JComponent parentComponent, String title) {
        return JOptionPane.showOptionDialog(
                parentComponent, panel, title, OK_CANCEL_OPTION, PLAIN_MESSAGE,
                null, null, null
        );
    }

    public Optional<E> show(JComponent parentComponent, String title) {
        int result = JOptionPane.showOptionDialog(parentComponent, panel, title,
                OK_CANCEL_OPTION, PLAIN_MESSAGE, null, null, null);

        while (result == OK_OPTION) {
            var entity = getEntity();
            var validation = entityValidator.validate(entity);

            if (validation.isValid()) {
                return Optional.of(entity);
            }

            showErrorMessages(validation.getValidationErrors());
            result = showOptionDialog(parentComponent, title);
        }

        return Optional.empty();
    }
}
