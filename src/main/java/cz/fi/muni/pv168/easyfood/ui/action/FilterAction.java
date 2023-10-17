package cz.fi.muni.pv168.easyfood.ui.action;


import cz.fi.muni.pv168.easyfood.ui.Icons;
import cz.fi.muni.pv168.easyfood.ui.tab.TabContainer;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public final class FilterAction extends AbstractAction {

    private final TabContainer tabContainer;

    public FilterAction(TabContainer tabContainer) {
        super("Filter", Icons.FILTER_ICON);
        this.tabContainer = tabContainer;
        putValue(SHORT_DESCRIPTION, "Filter recipes");
        putValue(MNEMONIC_KEY, KeyEvent.VK_F);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var dialog = tabContainer.getSelectedTab().getDialog().createNewDialog();
        var model = tabContainer.getSelectedTab().getModel();
        dialog.show(tabContainer.getComponent(), "Filter");
    }
}
