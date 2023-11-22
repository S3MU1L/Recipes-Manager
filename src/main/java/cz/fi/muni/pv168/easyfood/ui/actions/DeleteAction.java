package cz.fi.muni.pv168.easyfood.ui.actions;

import cz.fi.muni.pv168.easyfood.ui.I18N;
import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.tab.Tab;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.function.Supplier;


public final class DeleteAction extends AbstractAction {

    private static final I18N I18N = new I18N(DeleteAction.class);

    private final Supplier<Tab<?>> selectedTabSupplier;

    public DeleteAction(Supplier<Tab<?>> selectedTabSupplier) {
        super(I18N.getString("title"), Icons.DELETE_ICON);
        this.selectedTabSupplier = selectedTabSupplier;

        putValue(SHORT_DESCRIPTION, I18N.getString("description"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_D);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl D"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        selectedTabSupplier.get().deleteSelected();
    }
}
