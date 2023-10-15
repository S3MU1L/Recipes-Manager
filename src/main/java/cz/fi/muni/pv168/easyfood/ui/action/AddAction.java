package cz.fi.muni.pv168.easyfood.ui.action;


import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.tab.TabContainer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class AddAction extends AbstractAction {

    private final TabContainer tabContainer;

    public AddAction(TabContainer tabContainer) {
        super("Add", Icons.ADD_ICON);
        this.tabContainer = tabContainer;
        putValue(SHORT_DESCRIPTION, "Adds new recipe");
        putValue(MNEMONIC_KEY, KeyEvent.VK_A);
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("ctrl N"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var dialog = tabContainer.getSelectedTab().getDialog();
        var model = tabContainer.getSelectedTab().getModel();
        StringBuilder title = new StringBuilder("Add ").append(tabContainer.getSelectedTab().getTitle());
        title.deleteCharAt(title.length() - 1);
        dialog.show(tabContainer.getSelectedTab().getTable(), title.toString()).ifPresent(model::addRow);
    }
}
