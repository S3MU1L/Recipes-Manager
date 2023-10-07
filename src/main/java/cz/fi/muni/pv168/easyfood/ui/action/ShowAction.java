package cz.fi.muni.pv168.easyfood.ui.actions;

import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.tab.Tab;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.function.Supplier;

public class ShowAction extends AbstractAction {

    private static final I18N I18N = new I18N(ShowAction.class);

    private final Supplier<Tab<?>> selectedTabSupplier;

    public ShowAction(Supplier<Tab<?>> selectedTabSupplier) {
        super(I18N.getString("title"), Icons.SHOW_ICON);
        this.selectedTabSupplier = selectedTabSupplier;

        putValue(SHORT_DESCRIPTION, I18N.getString("description"));
        putValue(MNEMONIC_KEY, KeyEvent.VK_S);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl S"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        selectedTabSupplier.get().showSelected();
    }
}
