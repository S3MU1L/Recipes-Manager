package cz.fi.muni.pv168.easyfood.ui.actions;

import cz.fi.muni.pv168.easyfood.ui.I18N;
import cz.fi.muni.pv168.easyfood.ui.tab.Tab;
import cz.fi.muni.pv168.easyfood.ui.Icons;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.function.Supplier;

public final class EditAction extends AbstractAction {

    private static final I18N I18N = new I18N(EditAction.class);

    private final Supplier<Tab<?>> selectedTabSupplier;

    public EditAction(Supplier<Tab<?>> selectedTabSupplier) {
        super(I18N.getString("title"), Icons.EDIT_ICON);
        this.selectedTabSupplier = selectedTabSupplier;
        putValue(SHORT_DESCRIPTION, I18N.getString("description"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_E);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl E"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        selectedTabSupplier.get().updateSelected();
    }
}
